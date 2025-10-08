# Анализ программы
## Процедурный подход
В проекте был использован процедурный подход, то есть вся логика собрана в одном файле Main.kt: парсинг, доступ к базе данных, сценарий проверки и т.д.; нет абстракций, интерфейсов и слоев; CLI, формат аргументов смешаны со смысловой логикой программы (далее домен).
Из-за этого: проводить тестирование правил доступа отдельно от CLI невозможно, так как все смешано и завязано на exitProcess; любое изменение формата входных параметров или кодов возврата приведет к переписыванию одной большой функции; невозможно переиспользовать доменную логику.

Файл: src/main/kotlin/app/Main.kt
В одном файле смешаны:
парсинг аргументов (parseArgs, ArgParser),
доменные правила (checkPassword, effectivePermissions, проверка лимитов),
хранилище (object Db),
сценарий/оркестрация (main),
завершение процесса (exitProcess), вывод справки.

Следствие: домен нельзя тестировать отдельно от CLI и exitProcess, любое изменение интерфейса ввода/вывода приводит к правкам домена.

## Нарушения принципов SOLID
### Single Responsibility
Нарушен, так как в Main.kt: парсинг аргументов (инфраструктура), домен, доступ к данным, политика завершения процесса.
Можно исправить если разделить Main на разные классы: парсер CLI, юз-кейс проверки доступа, репозитории пользователей/ресурсов.

Где: 
main(): одновременно оркеструет сценарий, валидирует вход, работает с доменом, завершает процесс.
parseArgs(): логика парсинга CLI живет рядом с доменом.
object Db в Main.kt: и данные, и политика доступов/прав прямо в коде приложения.
checkPassword(), effectivePermissions() — доменные функции находятся в том же модуле, что и CLI и exitProcess.

Вариант исправления:
Разнести: kotlinx-cli, Use case: CheckAccess, AuthService, AccessPolicy, QuotaService, UserRepository, ResourceRepository, PermissionService.
main должен лишь связать адаптеры и вызвать use-case.

### Open/Closed
Нарушен, так как добавление нового действия требует менять when в main, замена источника данных невозможна без правок доменной логики.
Можно исправить если ввести интерфейсы UserRepository, ResourceRepository, PermissionService. Добавление новой реализации не меняет доменный код.

Где:
main(): добавление нового действия требует менять when (a.actionStr) {}.
Подмена источника данных: в object Db зашит в код, заменить его на файл/БД/HTTP нельзя без модификации домена.

Вариант исправления:
Ввести PermissionService, UserRepository, ResourceRepository как интерфейсы.
Маппинг строк CLI в Action вынести в слой адаптера; домен Action пусть не знает ничего о CLI-строках.

### Liskov Substitution
Нарушен, так как доменная логика жестко зависит от Db и CLI. Если позднее появятся альтернативные реализации, их нельзя будет подставить без переписывания. Это следствие отсутствия абстракций.
Можно исправить если строить зависимости от интерфейсов, а не от конкретных реализаций.

Где:
Явных иерархий нет, но подстановка реализаций по смыслу невозможна: домен и сценарий завязаны на конкретные реализации (Db, MessageDigest, CLI).
Если позже появится реализация UserRepository для БД — ее нельзя подставить без переписывания Db.
Вариант исправления:
Сначала ввести абстракции, затем работать через них. Тогда любая реализация (InMemory, Jdbc, Http) подставляется без изменения доменного кода.

### Interface Segregation
Нарушен, так как интерфейсов вообще нет — вся функциональность слиплась в процедуры. Клиенты вынуждены "все знать" так сказать.
Можно исправить если разделить контракты на маленькие интерфейсы: AuthService, PermissionService, QuotaService, CliIO (или Presenter), Clock и т.д.

Где:
Интерфейсов нет вообще — клиенты вынуждены "знать все": как парсится CLI, какие коды выхода, как проверяются права.

Вариант исправления:
Ввести узкие контракты:
AuthService, PermissionService, QuotaService, UserRepository, ResourceRepository, Hasher, Presenter/CliIO.
Каждый потребитель работает только с тем контрактом, который ему нужен

### Dependency Inversion
Нарушен, так как доменный код зависит от Db, kotlinx-cli, exitProcess.
Высокоуровневая политика (правила доступа) не отделена от низкоуровневых механизмов.

Где:
Доменный сценарий зависит от следующих деталей:
object Db,
MessageDigest напрямую,
exitProcess(…) (политика завершения процесса из домена).

Вариант исправления:
Зависеть от абстракций: UserRepository, ResourceRepository, PermissionService, Hasher, Presenter.
Реализации подключать снаружи в main (композиция), домен о них не знает.

# Чистая архитектура
На данный момент в реализации присутсвуют следующие проблемы:
Домен знает про CLI (ArgParser) и про политику завершения (exitProcess).
Доступ к данным (Db) — жестко зашит внутрь домена.
Смешение слоев приводит к низкой тестируемости: нельзя прогнать бизнес-правила без CLI.

Должно быть целевое разбиение, то есть:

Main.kt в пакете app

пакет usecase в котором CheckAccess.kt

пакет domain в котором: model (Action, User, Resource), ports (UserRepository, ResourceRepository, PermissionService, Hasher), services (AuthService, AccessPolicy, QuotaService)

пакет infrastructure в котором: cli (kotlinx-cli, Presenter - коды выхода), repo (InMemoryUserRepo, InMemoryResourceRepo, InMemoryPermissionService), crypto (Sha256Hasher)

Таким образом зависимости "направлены внутрь": инфраструктура, usecase, domain.
Домен ничего не знает о CLI, stdout/stderr, exitProcess, MessageDigest и конкретных способах хранения.
# План по рефакторингу

## 1 Модели в домен
Вынести Action, User, Resource в domain/model.

## 2 Интерфейсы
Создать в domain/ports:

interface UserRepository { fun find(login: String): User? }

interface ResourceRepository { fun find(path: String): Resource? }

interface PermissionService { fun effective(login: String, path: String): Set<Action> }

interface Hasher { fun sha256Hex(s: String): String }
## 3 Доменные сервисы
В domain/services:

AuthService (использует Hasher) → checkPassword.

AccessPolicy → effectivePermissions + проверка права.

QuotaService → проверка лимитов.

## 4 Use-case
В usecase/CheckAccess.kt:

DTO CheckAccessInput (login, password, action, resource, volume).

CheckAccessResult (sealed class: Ok, BadLogin, BadPassword, UnknownAction, NoAccess, ResourceNotFound, BadFormat, VolumeExceeded, Help).

execute(input): CheckAccessResult — без exitProcess.

## 5 Инфраструктура

infrastructure/repo: InMemoryUserRepo, InMemoryResourceRepo, InMemoryPermissionService (перенести содержимое Db).

infrastructure/crypto: Sha256Hasher (обертка над MessageDigest).

infrastructure/cli:

CliParser (kotlinx-cli) → CheckAccessInput (или сигнал Help).

CliPresenter — маппит CheckAccessResult в коды 0..8 и печатает help.

## 6 Main.kt

Собрать зависимости (композиция), вызвать CliParser, затем usecase.execute, затем CliPresenter.present(result) → exitProcess(code).

В main не должно быть доменной логики.

## 7 Тесты

Unit-тесты на CheckAccess с моками UserRepository, ResourceRepository, PermissionService, Hasher.

Отдельно протестировать CliParser (kotlinx-cli) и маппинг результата в коды.

## 8 Критерии готовности

Домен и use-case не импортируют kotlinx-cli, MessageDigest, exitProcess.

Main.kt содержит только связки.

В infrastructure минимум одна реализация портов (InMemory).

## Схема проекта:
```
src/
└── main/
    └── kotlin/
        ├── app/
        │   └── Main.kt
        │
        ├── domain/
        │   ├── model/
        │   │   ├── Action.kt
        │   │   ├── User.kt
        │   │   └── Resource.kt
        │   │
        │   ├── ports/
        │   │   ├── UserRepository.kt
        │   │   ├── ResourceRepository.kt
        │   │   ├── PermissionService.kt
        │   │   └── Hasher.kt
        │   │
        │   └── services/
        │       ├── AuthService.kt
        │       ├── AccessPolicy.kt
        │       └── QuotaService.kt
        │
        ├── usecase/
        │   └── CheckAccess.kt
        │
        └── infrastructure/
            ├── cli/
            │   ├── CliParser.kt
            │   └── CliPresenter.kt
            │
            ├── repo/
            │   ├── InMemoryUserRepository.kt
            │   ├── InMemoryResourceRepository.kt
            │   └── InMemoryPermissionService.kt
            │
            └── crypto/
                └── Sha256Hasher.kt
```
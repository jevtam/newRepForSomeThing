ALTER TABLE permissions
  ADD CONSTRAINT fk_perm_user
  FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_perm_user ON permissions(user_id);
CREATE INDEX idx_perm_resource_path ON permissions(resource_path);

ALTER TABLE permissions
  ADD CONSTRAINT uk_perm UNIQUE (user_id, resource_path);

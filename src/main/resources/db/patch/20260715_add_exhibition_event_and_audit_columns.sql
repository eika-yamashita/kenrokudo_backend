ALTER TABLE species
  ADD COLUMN IF NOT EXISTS create_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS update_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE species
SET
  create_user = COALESCE(create_user, 'system'),
  create_at = COALESCE(create_at, CURRENT_TIMESTAMP),
  update_user = COALESCE(update_user, 'system'),
  update_at = COALESCE(update_at, create_at, CURRENT_TIMESTAMP);

ALTER TABLE species
  ALTER COLUMN create_user SET NOT NULL,
  ALTER COLUMN create_at SET NOT NULL;

ALTER TABLE morph_master
  ADD COLUMN IF NOT EXISTS create_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS update_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE morph_master
SET
  create_user = COALESCE(create_user, 'system'),
  create_at = COALESCE(create_at, CURRENT_TIMESTAMP),
  update_user = COALESCE(update_user, 'system'),
  update_at = COALESCE(update_at, create_at, CURRENT_TIMESTAMP);

ALTER TABLE morph_master
  ALTER COLUMN create_user SET NOT NULL,
  ALTER COLUMN create_at SET NOT NULL;

ALTER TABLE bloodline_master
  ADD COLUMN IF NOT EXISTS create_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS update_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE bloodline_master
SET
  create_user = COALESCE(create_user, 'system'),
  create_at = COALESCE(create_at, CURRENT_TIMESTAMP),
  update_user = COALESCE(update_user, 'system'),
  update_at = COALESCE(update_at, create_at, CURRENT_TIMESTAMP);

ALTER TABLE bloodline_master
  ALTER COLUMN create_user SET NOT NULL,
  ALTER COLUMN create_at SET NOT NULL;

ALTER TABLE pairing
  ADD COLUMN IF NOT EXISTS create_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS update_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE pairing
SET
  create_user = COALESCE(create_user, 'system'),
  create_at = COALESCE(create_at, CURRENT_TIMESTAMP),
  update_user = COALESCE(update_user, 'system'),
  update_at = COALESCE(update_at, create_at, CURRENT_TIMESTAMP);

ALTER TABLE pairing
  ALTER COLUMN create_user SET NOT NULL,
  ALTER COLUMN create_at SET NOT NULL;

ALTER TABLE individual_image
  ADD COLUMN IF NOT EXISTS create_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS update_user VARCHAR(20),
  ADD COLUMN IF NOT EXISTS update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

DO $$
DECLARE
  has_created_by BOOLEAN;
  has_created_at BOOLEAN;
  has_updated_by BOOLEAN;
  has_updated_at BOOLEAN;
BEGIN
  SELECT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'individual_image' AND column_name = 'created_by'
  ) INTO has_created_by;

  SELECT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'individual_image' AND column_name = 'created_at'
  ) INTO has_created_at;

  SELECT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'individual_image' AND column_name = 'updated_by'
  ) INTO has_updated_by;

  SELECT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'individual_image' AND column_name = 'updated_at'
  ) INTO has_updated_at;

  IF has_created_by THEN
    EXECUTE 'UPDATE individual_image SET create_user = COALESCE(create_user, created_by, ''system'')';
  ELSE
    UPDATE individual_image SET create_user = COALESCE(create_user, 'system');
  END IF;

  IF has_created_at THEN
    EXECUTE 'UPDATE individual_image SET create_at = COALESCE(create_at, created_at, CURRENT_TIMESTAMP)';
  ELSE
    UPDATE individual_image SET create_at = COALESCE(create_at, CURRENT_TIMESTAMP);
  END IF;

  IF has_updated_by AND has_created_by THEN
    EXECUTE 'UPDATE individual_image SET update_user = COALESCE(update_user, updated_by, created_by, ''system'')';
  ELSIF has_updated_by THEN
    EXECUTE 'UPDATE individual_image SET update_user = COALESCE(update_user, updated_by, ''system'')';
  ELSIF has_created_by THEN
    EXECUTE 'UPDATE individual_image SET update_user = COALESCE(update_user, created_by, ''system'')';
  ELSE
    UPDATE individual_image SET update_user = COALESCE(update_user, 'system');
  END IF;

  IF has_updated_at AND has_created_at THEN
    EXECUTE 'UPDATE individual_image SET update_at = COALESCE(update_at, updated_at, create_at, created_at, CURRENT_TIMESTAMP)';
  ELSIF has_updated_at THEN
    EXECUTE 'UPDATE individual_image SET update_at = COALESCE(update_at, updated_at, create_at, CURRENT_TIMESTAMP)';
  ELSIF has_created_at THEN
    EXECUTE 'UPDATE individual_image SET update_at = COALESCE(update_at, create_at, created_at, CURRENT_TIMESTAMP)';
  ELSE
    UPDATE individual_image SET update_at = COALESCE(update_at, create_at, CURRENT_TIMESTAMP);
  END IF;
END $$;

ALTER TABLE individual_image
  ALTER COLUMN create_user SET NOT NULL,
  ALTER COLUMN create_at SET NOT NULL,
  DROP COLUMN IF EXISTS created_by,
  DROP COLUMN IF EXISTS created_at,
  DROP COLUMN IF EXISTS updated_by,
  DROP COLUMN IF EXISTS updated_at;

CREATE TABLE IF NOT EXISTS exhibition_event (
  event_id BIGSERIAL NOT NULL,
  event_name VARCHAR(100) NOT NULL,
  event_date DATE NOT NULL,
  area VARCHAR(50),
  start_time TIME,
  end_time TIME,
  venue VARCHAR(255),
  event_url VARCHAR(500),
  note VARCHAR(500),
  display_order INTEGER NOT NULL DEFAULT 0,
  is_published BOOLEAN NOT NULL DEFAULT TRUE,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (event_id)
);

CREATE INDEX IF NOT EXISTS idx_exhibition_event_upcoming
  ON exhibition_event (is_published, event_date, start_time, display_order);

ALTER TABLE exhibition_event
  ADD COLUMN IF NOT EXISTS area VARCHAR(50);

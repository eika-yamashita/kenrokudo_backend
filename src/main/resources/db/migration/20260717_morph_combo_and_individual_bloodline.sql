BEGIN;

ALTER TABLE morph_master
  ADD COLUMN morph_type VARCHAR(10) NOT NULL DEFAULT 'SINGLE',
  ADD COLUMN display_priority INTEGER NOT NULL DEFAULT 0;

ALTER TABLE morph_master
  DROP CONSTRAINT ck_morph_master_inheritance,
  ALTER COLUMN inheritance_category DROP NOT NULL,
  ADD CONSTRAINT ck_morph_master_type CHECK (morph_type IN ('SINGLE', 'COMBO')),
  ADD CONSTRAINT ck_morph_master_inheritance CHECK (
    (morph_type = 'SINGLE' AND inheritance_category IN ('0', '1', '2', '3')) OR
    (morph_type = 'COMBO' AND inheritance_category IS NULL)
  );

CREATE TABLE combo_morph_component (
  species_id VARCHAR(20) NOT NULL,
  combo_morph_id VARCHAR(20) NOT NULL,
  component_morph_id VARCHAR(20) NOT NULL,
  required_expression CHAR(1) NOT NULL DEFAULT '0',
  sort_order INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (species_id, combo_morph_id, component_morph_id),
  CONSTRAINT fk_combo_morph_component_combo
    FOREIGN KEY (species_id, combo_morph_id)
    REFERENCES morph_master (species_id, morph_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_combo_morph_component_single
    FOREIGN KEY (species_id, component_morph_id)
    REFERENCES morph_master (species_id, morph_id),
  CONSTRAINT ck_combo_morph_component_expression CHECK (required_expression = '0')
);

CREATE TABLE individual_bloodline (
  species_id VARCHAR(20) NOT NULL,
  individual_id VARCHAR(20) NOT NULL,
  morph_id VARCHAR(20) NOT NULL,
  bloodline_id VARCHAR(20) NOT NULL,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id, individual_id, morph_id),
  CONSTRAINT fk_individual_bloodline_individual
    FOREIGN KEY (species_id, individual_id)
    REFERENCES individual (species_id, id)
    ON DELETE CASCADE,
  CONSTRAINT fk_individual_bloodline_master
    FOREIGN KEY (species_id, morph_id, bloodline_id)
    REFERENCES bloodline_master (species_id, morph_id, bloodline_id)
);

INSERT INTO individual_bloodline (
  species_id, individual_id, morph_id, bloodline_id,
  create_user, create_at, update_user, update_at
)
SELECT
  species_id, individual_id, morph_id, bloodline_id,
  create_user, create_at, update_user, update_at
FROM individual_morph
WHERE expression_category = '0'
  AND bloodline_id IS NOT NULL;

ALTER TABLE individual_morph
  DROP CONSTRAINT fk_individual_morph_bloodline,
  DROP COLUMN bloodline_id;

COMMIT;

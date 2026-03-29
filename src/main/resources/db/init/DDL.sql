CREATE TABLE species (
  species_id VARCHAR(20) NOT NULL,
  japanese_name VARCHAR(100) NOT NULL,
  common_name VARCHAR(100),
  english_name VARCHAR(100),
  total_length VARCHAR(50),
  body_weight VARCHAR(50),
  lifespan VARCHAR(50),
  PRIMARY KEY (species_id)
);

CREATE TABLE individual (
  species_cd VARCHAR(20) NOT NULL,
  id VARCHAR(20) NOT NULL,
  male_parent_id VARCHAR(20),
  female_parent_id VARCHAR(20),
  morph VARCHAR(50),
  bloodline VARCHAR(50),
  gender_category CHAR(1),
  breeding_category CHAR(1),
  breeder VARCHAR(20),
  clutch_date DATE,
  hatch_date DATE,
  purchase_from VARCHAR(255),
  purchase_price DECIMAL(15,2),
  purchase_date DATE,
  sales_category VARCHAR(20),
  sales_to VARCHAR(255),
  sales_price_tax_ex DECIMAL(15,2),
  sales_price_tax DECIMAL(15,2),
  sales_price_tax_in DECIMAL(15,2),
  sales_date DATE,
  death_date DATE,
  note VARCHAR(255),
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_cd, id),
  CONSTRAINT fk_individual_species
    FOREIGN KEY (species_cd)
    REFERENCES species (species_id)
);

CREATE TABLE individual_image (
  image_id BIGSERIAL NOT NULL,
  species_cd VARCHAR(20) NOT NULL,
  individual_id VARCHAR(20) NOT NULL,
  storage_path VARCHAR(255) NOT NULL,
  public_url VARCHAR(255) NOT NULL,
  file_name VARCHAR(255),
  content_type VARCHAR(100) NOT NULL,
  file_size BIGINT NOT NULL,
  sort_order INTEGER NOT NULL DEFAULT 0,
  is_primary BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(20),
  updated_at TIMESTAMP,
  updated_by VARCHAR(20),
  PRIMARY KEY (image_id),
  CONSTRAINT fk_individual_image_individual
    FOREIGN KEY (species_cd, individual_id)
    REFERENCES individual (species_cd, id)
    ON DELETE CASCADE
);

CREATE INDEX idx_individual_image_individual
  ON individual_image (species_cd, individual_id);

CREATE TABLE species (
  species_id VARCHAR(20) NOT NULL,
  japanese_name VARCHAR(100) NOT NULL,
  common_name VARCHAR(100),
  english_name VARCHAR(100),
  total_length VARCHAR(50),
  body_weight VARCHAR(50),
  lifespan VARCHAR(50),
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id)
);

CREATE TABLE morph_master (
  species_id VARCHAR(20) NOT NULL,
  morph_id VARCHAR(20) NOT NULL,
  morph_name VARCHAR(100) NOT NULL,
  morph_type VARCHAR(10) NOT NULL DEFAULT 'SINGLE',
  inheritance_category CHAR(1),
  display_priority INTEGER NOT NULL DEFAULT 0,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id, morph_id),
  CONSTRAINT uq_morph_master_name UNIQUE (species_id, morph_name),
  CONSTRAINT fk_morph_master_species FOREIGN KEY (species_id) REFERENCES species (species_id),
  CONSTRAINT ck_morph_master_type CHECK (morph_type IN ('SINGLE', 'COMBO')),
  CONSTRAINT ck_morph_master_inheritance CHECK (
    (morph_type = 'SINGLE' AND inheritance_category IN ('0', '1', '2', '3', '9')) OR
    (morph_type = 'COMBO' AND inheritance_category IS NULL)
  )
);

CREATE INDEX idx_morph_master_species
  ON morph_master (species_id, morph_id);

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

CREATE TABLE bloodline_master (
  species_id VARCHAR(20) NOT NULL,
  morph_id VARCHAR(20) NOT NULL,
  bloodline_id VARCHAR(20) NOT NULL,
  bloodline_name VARCHAR(100) NOT NULL,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id, morph_id, bloodline_id),
  CONSTRAINT uq_bloodline_master_name UNIQUE (species_id, morph_id, bloodline_name),
  CONSTRAINT fk_bloodline_master_morph
    FOREIGN KEY (species_id, morph_id)
    REFERENCES morph_master (species_id, morph_id)
);

CREATE INDEX idx_bloodline_master_morph
  ON bloodline_master (species_id, morph_id, bloodline_id);

CREATE TABLE individual (
  species_id VARCHAR(20) NOT NULL,
  fiscal_year INTEGER NOT NULL,
  id VARCHAR(20) NOT NULL,
  pairing_fiscal_year INTEGER,
  pairing_id VARCHAR(20),
  male_parent_id VARCHAR(20),
  female_parent_id VARCHAR(20),
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
  PRIMARY KEY (species_id, id),
  CONSTRAINT fk_individual_species FOREIGN KEY (species_id) REFERENCES species (species_id)
);

CREATE TABLE pairing (
  species_id VARCHAR(20) NOT NULL,
  fiscal_year INTEGER NOT NULL,
  pairing_id VARCHAR(20) NOT NULL,
  male_parent_id VARCHAR(20) NOT NULL,
  female_parent_id VARCHAR(20) NOT NULL,
  pairing_date DATE NOT NULL,
  note VARCHAR(255),
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id, fiscal_year, pairing_id),
  CONSTRAINT uq_pairing_signature UNIQUE (species_id, male_parent_id, female_parent_id, pairing_date),
  CONSTRAINT fk_pairing_species FOREIGN KEY (species_id) REFERENCES species (species_id),
  CONSTRAINT fk_pairing_male_parent FOREIGN KEY (species_id, male_parent_id) REFERENCES individual (species_id, id),
  CONSTRAINT fk_pairing_female_parent FOREIGN KEY (species_id, female_parent_id) REFERENCES individual (species_id, id)
);

CREATE INDEX idx_pairing_species_year
  ON pairing (species_id, fiscal_year);

ALTER TABLE individual
  ADD CONSTRAINT fk_individual_pairing
  FOREIGN KEY (species_id, pairing_fiscal_year, pairing_id)
  REFERENCES pairing (species_id, fiscal_year, pairing_id);

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

CREATE TABLE individual_morph (
  species_id VARCHAR(20) NOT NULL,
  individual_id VARCHAR(20) NOT NULL,
  morph_id VARCHAR(20) NOT NULL,
  expression_category CHAR(1) NOT NULL,
  possible_het_percentage INTEGER,
  sort_order INTEGER NOT NULL DEFAULT 0,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (species_id, individual_id, morph_id, expression_category),
  CONSTRAINT fk_individual_morph_individual
    FOREIGN KEY (species_id, individual_id)
    REFERENCES individual (species_id, id)
    ON DELETE CASCADE,
  CONSTRAINT fk_individual_morph_master
    FOREIGN KEY (species_id, morph_id)
    REFERENCES morph_master (species_id, morph_id),
  CONSTRAINT ck_individual_morph_expression CHECK (expression_category IN ('0', '1', '2')),
  CONSTRAINT ck_individual_morph_percentage CHECK (
    possible_het_percentage IS NULL OR
    (possible_het_percentage >= 0 AND possible_het_percentage <= 100)
  )
);

CREATE INDEX idx_individual_morph_individual
  ON individual_morph (species_id, individual_id, expression_category, sort_order);

CREATE TABLE individual_image (
  image_id BIGSERIAL NOT NULL,
  species_id VARCHAR(20) NOT NULL,
  individual_id VARCHAR(20) NOT NULL,
  storage_path VARCHAR(255) NOT NULL,
  public_url VARCHAR(255) NOT NULL,
  file_name VARCHAR(255),
  content_type VARCHAR(100) NOT NULL,
  file_size BIGINT NOT NULL,
  sort_order INTEGER NOT NULL DEFAULT 0,
  is_primary BOOLEAN NOT NULL DEFAULT FALSE,
  create_user VARCHAR(20) NOT NULL,
  create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_user VARCHAR(20),
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (image_id),
  CONSTRAINT fk_individual_image_individual
    FOREIGN KEY (species_id, individual_id)
    REFERENCES individual (species_id, id)
    ON DELETE CASCADE
);

CREATE INDEX idx_individual_image_individual
  ON individual_image (species_id, individual_id);

CREATE TABLE exhibition_event (
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

CREATE INDEX idx_exhibition_event_upcoming
  ON exhibition_event (is_published, event_date, start_time, display_order);

COMMENT ON TABLE species IS '種マスタ';
COMMENT ON COLUMN species.species_id IS '種コード';
COMMENT ON COLUMN species.japanese_name IS '和名';
COMMENT ON COLUMN species.common_name IS '通称';
COMMENT ON COLUMN species.english_name IS '英名';
COMMENT ON COLUMN species.total_length IS '全長';
COMMENT ON COLUMN species.body_weight IS '体重';
COMMENT ON COLUMN species.lifespan IS '寿命';

COMMENT ON TABLE morph_master IS 'モルフマスタ';
COMMENT ON COLUMN morph_master.species_id IS '種コード';
COMMENT ON COLUMN morph_master.morph_id IS 'モルフID';
COMMENT ON COLUMN morph_master.morph_name IS 'モルフ名';
COMMENT ON COLUMN morph_master.inheritance_category IS '遺伝性区分 0:多因子遺伝、1:劣性遺伝、2:優性遺伝、3:共優性遺伝、9:該当なし';

COMMENT ON TABLE bloodline_master IS '血統マスタ';
COMMENT ON COLUMN bloodline_master.species_id IS '種コード';
COMMENT ON COLUMN bloodline_master.morph_id IS 'モルフID';
COMMENT ON COLUMN bloodline_master.bloodline_id IS '血統ID';
COMMENT ON COLUMN bloodline_master.bloodline_name IS '血統名';

COMMENT ON TABLE individual IS '個体情報';
COMMENT ON COLUMN individual.species_id IS '種コード';
COMMENT ON COLUMN individual.fiscal_year IS '登録年度';
COMMENT ON COLUMN individual.id IS '個体ID';
COMMENT ON COLUMN individual.pairing_fiscal_year IS 'ペアリング年度';
COMMENT ON COLUMN individual.pairing_id IS 'ペアリングID';
COMMENT ON COLUMN individual.male_parent_id IS 'オス親ID';
COMMENT ON COLUMN individual.female_parent_id IS 'メス親ID';
COMMENT ON COLUMN individual.gender_category IS '性別区分 0:不明、1:オス、2:メス';
COMMENT ON COLUMN individual.breeding_category IS '繁殖区分 0:自家繁殖、1:購入個体';
COMMENT ON COLUMN individual.breeder IS 'ブリーダー';
COMMENT ON COLUMN individual.clutch_date IS 'クラッチ日';
COMMENT ON COLUMN individual.hatch_date IS 'ハッチ日';
COMMENT ON COLUMN individual.purchase_from IS '購入元';
COMMENT ON COLUMN individual.purchase_price IS '購入価格';
COMMENT ON COLUMN individual.purchase_date IS '購入日';
COMMENT ON COLUMN individual.sales_category IS '販売区分 0:非売個体、1:販売中、2:販売済み';
COMMENT ON COLUMN individual.sales_to IS '販売先';
COMMENT ON COLUMN individual.sales_price_tax_ex IS '販売価格(税抜)';
COMMENT ON COLUMN individual.sales_price_tax IS '消費税額';
COMMENT ON COLUMN individual.sales_price_tax_in IS '販売価格(税込)';
COMMENT ON COLUMN individual.sales_date IS '販売日';
COMMENT ON COLUMN individual.death_date IS '死亡日';
COMMENT ON COLUMN individual.note IS 'メモ';
COMMENT ON COLUMN individual.create_user IS '作成者';
COMMENT ON COLUMN individual.create_at IS '作成日時';
COMMENT ON COLUMN individual.update_user IS '更新者';
COMMENT ON COLUMN individual.update_at IS '更新日時';

COMMENT ON TABLE pairing IS 'ペアリング情報';
COMMENT ON COLUMN pairing.species_id IS '種コード';
COMMENT ON COLUMN pairing.fiscal_year IS 'ペアリング年度';
COMMENT ON COLUMN pairing.pairing_id IS 'ペアリングID';
COMMENT ON COLUMN pairing.male_parent_id IS 'オス親ID';
COMMENT ON COLUMN pairing.female_parent_id IS 'メス親ID';
COMMENT ON COLUMN pairing.pairing_date IS 'ペアリング日';
COMMENT ON COLUMN pairing.note IS 'メモ';

COMMENT ON TABLE individual_morph IS '個体モルフ情報';
COMMENT ON COLUMN individual_morph.species_id IS '種コード';
COMMENT ON COLUMN individual_morph.individual_id IS '個体ID';
COMMENT ON COLUMN individual_morph.morph_id IS 'モルフID';
COMMENT ON COLUMN individual_morph.expression_category IS '表現区分 0:visual、1:het、2:possible_het';
COMMENT ON COLUMN individual_morph.possible_het_percentage IS 'Possヘテロ率';
COMMENT ON COLUMN individual_morph.sort_order IS '表示順';
COMMENT ON COLUMN individual_morph.create_user IS '作成者';
COMMENT ON COLUMN individual_morph.create_at IS '作成日時';
COMMENT ON COLUMN individual_morph.update_user IS '更新者';
COMMENT ON COLUMN individual_morph.update_at IS '更新日時';

COMMENT ON TABLE individual_image IS '個体画像';
COMMENT ON COLUMN individual_image.image_id IS '画像ID';
COMMENT ON COLUMN individual_image.species_id IS '種コード';
COMMENT ON COLUMN individual_image.individual_id IS '個体ID';
COMMENT ON COLUMN individual_image.storage_path IS '保存パス';
COMMENT ON COLUMN individual_image.public_url IS '公開URL';
COMMENT ON COLUMN individual_image.file_name IS 'ファイル名';
COMMENT ON COLUMN individual_image.content_type IS 'Content-Type';
COMMENT ON COLUMN individual_image.file_size IS 'ファイルサイズ';
COMMENT ON COLUMN individual_image.sort_order IS '表示順';
COMMENT ON COLUMN individual_image.is_primary IS 'メイン画像フラグ';
COMMENT ON COLUMN individual_image.create_user IS '作成者';
COMMENT ON COLUMN individual_image.create_at IS '作成日時';
COMMENT ON COLUMN individual_image.update_user IS '更新者';
COMMENT ON COLUMN individual_image.update_at IS '更新日時';

COMMENT ON TABLE exhibition_event IS '出店イベント';
COMMENT ON COLUMN exhibition_event.event_id IS 'イベントID';
COMMENT ON COLUMN exhibition_event.event_name IS 'イベント名';
COMMENT ON COLUMN exhibition_event.event_date IS 'イベント日';
COMMENT ON COLUMN exhibition_event.start_time IS '開始時間';
COMMENT ON COLUMN exhibition_event.end_time IS '終了時間';
COMMENT ON COLUMN exhibition_event.venue IS '場所';
COMMENT ON COLUMN exhibition_event.event_url IS 'URL';
COMMENT ON COLUMN exhibition_event.note IS '備考';
COMMENT ON COLUMN exhibition_event.display_order IS '表示順';
COMMENT ON COLUMN exhibition_event.is_published IS '公開フラグ';
COMMENT ON COLUMN exhibition_event.create_user IS '作成者';
COMMENT ON COLUMN exhibition_event.create_at IS '作成日時';
COMMENT ON COLUMN exhibition_event.update_user IS '更新者';
COMMENT ON COLUMN exhibition_event.update_at IS '更新日時';

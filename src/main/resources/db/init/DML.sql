INSERT INTO species (
  species_id,
  japanese_name,
  common_name,
  english_name,
  total_length,
  body_weight,
  lifespan
) VALUES
  ('0001', 'ヒョウモントカゲモドキ', 'レオパ', 'Leopard Gecko', '18-28cm', '45-80g', '10-20年'),
  ('0002', 'ニシアフリカトカゲモドキ', 'ニシアフ', 'African Fat-Tailed Gecko', '18-25cm', '45-75g', '10-18年');

INSERT INTO individual (
  species_id,
  id,
  male_parent_id,
  female_parent_id,
  morph,
  bloodline,
  gender_category,
  breeding_category,
  breeder,
  clutch_date,
  hatch_date,
  purchase_from,
  purchase_price,
  purchase_date,
  sales_category,
  sales_to,
  sales_price_tax_ex,
  sales_price_tax,
  sales_price_tax_in,
  sales_date,
  death_date,
  note,
  create_user,
  create_at,
  update_user,
  update_at
) VALUES
  ('0001', '25M001', NULL, NULL, 'Mack Snow', 'US Line', '1', '1', 'North Crest Geckos', NULL, '2024-06-15', 'Tokyo Reptiles', 42000.00, '2025-01-12', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2025繁殖の種オス', 'system', '2025-01-12 10:00:00', 'system', '2025-01-12 10:00:00'),
  ('0001', '25F001', NULL, NULL, 'Bell Albino', 'EU Line', '0', '1', 'Amber Geckos', NULL, '2024-07-02', 'Osaka Gecko Farm', 46000.00, '2025-01-14', '0', NULL, NULL, NULL, NULL, NULL, NULL, '2025繁殖の種メス', 'system', '2025-01-14 11:00:00', 'system', '2025-01-14 11:00:00'),
  ('0001', '25A001', '25M001', '25F001', 'Mack Snow Bell', 'House Holdback', '0', '0', '絢禄堂', '2025-03-02', '2025-05-08', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '自家繁殖の非売個体', 'system', '2025-05-08 09:30:00', 'system', '2025-05-08 09:30:00'),
  ('0001', '25A002', '25M001', '25F001', 'Mack Snow Bell', 'House Holdback', '1', '0', '絢禄堂', '2025-03-02', '2025-05-10', NULL, NULL, NULL, '1', NULL, 28000.00, 2800.00, 30800.00, NULL, NULL, '自家繁殖の販売中個体', 'system', '2025-05-10 09:45:00', 'system', '2025-08-01 14:00:00'),
  ('0001', '25A003', '25M001', '25F001', 'Bell Enigma', 'House Select', '0', '0', '絢禄堂', '2025-03-02', '2025-05-14', NULL, NULL, NULL, '2', 'Nagoya Reptile Base', 32000.00, 3200.00, 35200.00, '2025-09-12', NULL, '自家繁殖の販売済み個体', 'system', '2025-05-14 10:10:00', 'system', '2025-09-12 18:20:00'),
  ('0001', '25A004', '25M001', '25F001', 'Tremper Albino', 'Project T', '1', '0', '絢禄堂', '2025-03-02', '2025-05-16', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, '2025-11-12', '死亡日確認用の個体', 'system', '2025-05-16 10:30:00', 'system', '2025-11-12 08:00:00'),
  ('0001', '26A001', '25M001', '25F001', 'Bell Stripe', 'House Select', '0', '0', '絢禄堂', '2026-02-14', '2026-04-20', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '2026自家繁殖の非売個体', 'system', '2026-04-20 09:20:00', 'system', '2026-04-20 09:20:00'),
  ('0001', '26A002', '25M001', '25F001', 'Mack Snow', 'House Select', '1', '0', '絢禄堂', '2026-02-14', '2026-04-22', NULL, NULL, NULL, '1', NULL, 30000.00, 3000.00, 33000.00, NULL, NULL, '2026自家繁殖の販売中個体', 'system', '2026-04-22 09:40:00', 'system', '2026-06-01 13:00:00'),
  ('0001', '26A003', '25M001', '25F001', 'Bell Sunglow', 'House Select', '0', '0', '絢禄堂', '2026-02-14', '2026-04-25', NULL, NULL, NULL, '2', 'Shibuya Exotics', 34000.00, 3400.00, 37400.00, '2026-07-18', NULL, '2026自家繁殖の販売済み個体', 'system', '2026-04-25 10:10:00', 'system', '2026-07-18 16:50:00'),
  ('0001', '26A004', NULL, NULL, 'Murphy Patternless', 'CA Import', '0', '1', 'Sunset Geckos', NULL, '2025-10-10', 'Kyoto Reptile Market', 38000.00, '2026-01-18', '2', 'Fukuoka Reptile House', 43000.00, 4300.00, 47300.00, '2026-03-10', NULL, '購入個体かつ販売済み確認用', 'system', '2026-01-18 12:00:00', 'system', '2026-03-10 18:30:00'),
  ('0001', '26A005', NULL, NULL, 'Super Hypo', 'Domestic Select', '1', '1', 'Maple Geckos', NULL, '2025-09-01', 'Yokohama Gecko Works', 36000.00, '2026-02-05', '1', NULL, 41000.00, 4100.00, 45100.00, NULL, NULL, '購入個体かつ販売中確認用', 'system', '2026-02-05 15:10:00', 'system', '2026-05-01 09:30:00'),
  ('0002', '25M001', NULL, NULL, 'Whiteout', 'Imported Line', '1', '1', 'Savanna Geckos', NULL, '2024-08-08', 'Tropical Supplier', 28000.00, '2025-02-01', '0', NULL, NULL, NULL, NULL, NULL, NULL, 'ニシアフの種オス', 'system', '2025-02-01 10:20:00', 'system', '2025-02-01 10:20:00'),
  ('0002', '25F001', NULL, NULL, 'Oreo', 'Imported Line', '0', '1', 'Savanna Geckos', NULL, '2024-09-12', 'Tropical Supplier', 30000.00, '2025-02-01', '0', NULL, NULL, NULL, NULL, NULL, NULL, 'ニシアフの種メス', 'system', '2025-02-01 10:25:00', 'system', '2025-02-01 10:25:00'),
  ('0002', '25A001', NULL, NULL, 'Amel', 'Imported Line', '0', '1', 'Savanna Geckos', NULL, '2024-10-01', 'West Reptile Hub', 22000.00, '2025-04-18', '2', 'Sapporo Exotic Shop', 26000.00, 2600.00, 28600.00, '2025-06-02', NULL, '購入個体の販売済み確認用', 'system', '2025-04-18 09:00:00', 'system', '2025-06-02 18:00:00'),
  ('0002', '26A001', '25M001', '25F001', 'Whiteout Oreo', 'House Project', '1', '0', '絢禄堂', '2026-01-30', '2026-03-28', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, 'ニシアフ自家繁殖の非売個体', 'system', '2026-03-28 08:45:00', 'system', '2026-03-28 08:45:00'),
  ('0002', '26A002', '25M001', '25F001', 'Whiteout Het Oreo', 'House Project', '0', '0', '絢禄堂', '2026-01-30', '2026-03-30', NULL, NULL, NULL, '1', NULL, 24000.00, 2400.00, 26400.00, NULL, NULL, 'ニシアフ自家繁殖の販売中個体', 'system', '2026-03-30 09:00:00', 'system', '2026-05-12 10:40:00');

INSERT INTO pairing (
  species_id,
  fiscal_year,
  pairing_id,
  male_parent_id,
  female_parent_id,
  pairing_date,
  note
) VALUES
  ('0001', 2025, 'A', '25M001', '25F001', '2025-02-10', '2025春のメインペア'),
  ('0001', 2025, 'B', '25M001', '25F001', '2025-09-05', '2025秋の再ペアリング'),
  ('0001', 2026, 'A', '25M001', '25F001', '2026-01-25', '2026春のメインペア'),
  ('0001', 2026, 'B', '25M001', '25F001', '2026-03-12', '2026追加クラッチ用'),
  ('0002', 2025, 'A', '25M001', '25F001', '2025-04-10', 'ニシアフ2025メインペア'),
  ('0002', 2026, 'A', '25M001', '25F001', '2026-01-18', 'ニシアフ2026メインペア');

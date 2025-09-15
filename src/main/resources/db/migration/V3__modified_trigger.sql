CREATE OR REPLACE FUNCTION validate_sale_allocation()
RETURNS TRIGGER AS $$
DECLARE
batch_exists BOOLEAN;
store_id_val BIGINT;
batch_id_val BIGINT;
BEGIN
IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
-- Lấy store_id từ sale_order qua sale_line
SELECT so.store_id INTO store_id_val
FROM sale_line sl
         JOIN sale_order so ON so.id = sl.sale_order_id
WHERE sl.id = NEW.sale_line_id
  AND sl.deleted = false
  AND so.deleted = false;
IF NOT FOUND THEN
RAISE EXCEPTION 'Sale line % not found or deleted', NEW.sale_line_id;
END IF;
-- Lấy batch_id từ batch_item và kiểm tra tồn tại
SELECT bi.batch_id INTO batch_id_val
FROM batch_item bi
WHERE bi.id = NEW.batch_item_id
  AND bi.deleted = false;
IF NOT FOUND THEN
RAISE EXCEPTION 'Batch item % does not exist or deleted', NEW.batch_item_id;
END IF;
-- Kiểm tra batch_stock tồn tại và active cho batch và store
SELECT EXISTS(
    SELECT 1 FROM batch_stock bs
    WHERE bs.batch_id = batch_id_val
      AND bs.store_id = store_id_val
      AND bs.status = 'ACTIVE'
      AND bs.deleted = false
) INTO batch_exists;
IF NOT batch_exists THEN
RAISE EXCEPTION 'Batch stock for batch % in store % is not active or does not exist', batch_id_val, store_id_val;
END IF;
-- Auto-fill unit_cost_snap nếu = 0 hoặc NULL
IF NEW.unit_cost_snap = 0 OR NEW.unit_cost_snap IS NULL THEN
SELECT bi.import_price INTO NEW.unit_cost_snap
FROM batch_item bi
WHERE bi.id = NEW.batch_item_id
    LIMIT 1;
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE batch_item
ADD COLUMN IF NOT EXISTS remain_qty INTEGER NOT NULL DEFAULT 0;

ALTER TABLE batch_item
    ADD CONSTRAINT chk_batch_item_remain_qty_valid
        CHECK (remain_qty >= 0 AND remain_qty <= original_qty);
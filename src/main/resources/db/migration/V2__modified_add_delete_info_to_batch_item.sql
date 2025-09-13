ALTER TABLE batch_item
ADD COLUMN "deleted_at" TIMESTAMP;
ALTER TABLE batch_item
ADD COLUMN "deleted_by" BIGINT;
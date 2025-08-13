package ck4.nvb.rsmanagement.core.module.stores.importlog.service.dto;

import ck4.nvb.rsmanagement.base.application.dto.CreateInput;
import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.UpdateInput;
import ck4.nvb.rsmanagement.core.module.stores.importlog.domain.ImportLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Getter @Setter
public class ImportLogDto extends EntityDto<String> implements CreateInput<ImportLog>, UpdateInput<ImportLog> {

    private Long fromStock;

    private Long toStore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date deliveryDate;

    private String status;

    @Override
    public ImportLog mapToEntity() {
        return new ModelMapper().map(this, ImportLog.class);
    }

    @Override
    public boolean mapToEntity(ImportLog entity) {
        return false;
    }
}

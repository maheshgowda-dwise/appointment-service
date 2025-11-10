package com.lifetrenz.lths.appointment.mapper;

import com.lifetrenz.lths.appointment.dto.BlockSchedularDto;
import com.lifetrenz.lths.appointment.dto.SchedulerEventDto;
import com.lifetrenz.lths.appointment.model.collection.BlockSchedular;
import com.lifetrenz.lths.appointment.model.collection.BlockSchedularEvent;
import com.lifetrenz.lths.appointment.model.collection.SchedulerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleEventMapper {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleEventMapper.class);

    public SchedulerEventDto mapToDto(SchedulerEvent entity) {
        if (entity == null) {
            logger.error("SchedulerEvent entity is null");
            throw new IllegalArgumentException("SchedulerEvent entity cannot be null");
        }

        return new SchedulerEventDto(
                entity.getId(),
                entity.getEventData(),
                entity.getReferrenceId(),
                entity.getChangedRecords(),
                entity.getScheduleType(),
                entity.getSchedularTransactionDetails()
        );
    }

    public BlockSchedularDto mapToBlockSchedularDto(BlockSchedular eventBlock) {
        return mapToBlockSchedularDtoCommon(eventBlock, null);
    }

    public BlockSchedularDto mapToBlockSchedularEventDto(BlockSchedularEvent eventBlock) {
        return mapToBlockSchedularDtoCommon(eventBlock, eventBlock.getEventIdentifier());
    }

    private BlockSchedularDto mapToBlockSchedularDtoCommon(Object eventBlock, String eventIdentifier) {
        if (eventBlock == null) {
            logger.error("Event block is null");
            throw new IllegalArgumentException("Event block cannot be null");
        }

        if (eventBlock instanceof BlockSchedular) {
            BlockSchedular block = (BlockSchedular) eventBlock;
            return new BlockSchedularDto(
                    block.getId(),
                    block.getUserName(),
                    block.getUserId(),
                    block.getPatientName(),
                    block.getPatientId(),
                    block.getSchedularEventId(),
                    eventIdentifier,
                    block.getBlockedDate(),
                    block.getStatus(),
                    block.getCustomerTrasaction()
            );
        } else if (eventBlock instanceof BlockSchedularEvent) {
            BlockSchedularEvent blockEvent = (BlockSchedularEvent) eventBlock;
            return new BlockSchedularDto(
                    blockEvent.getId(),
                    blockEvent.getUserName(),
                    blockEvent.getUserId(),
                    blockEvent.getPatientName(),
                    blockEvent.getPatientId(),
                    blockEvent.getSchedularEventId(),
                    eventIdentifier,
                    blockEvent.getBlockedDate(),
                    blockEvent.getStatus(),
                    blockEvent.getCustomerTrasaction()
            );
        } else {
            logger.error("Unsupported event block type: {}", eventBlock.getClass().getName());
            throw new IllegalArgumentException("Unsupported event block type");
        }
    }
}

package dk.demo.jpaoptimisticlockingpoc.converter;

import dk.demo.jpaoptimisticlockingpoc.dto.DrumsDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Drums;
import org.springframework.stereotype.Service;

@Service
public class DrumsToDtoConverter extends AbstractToDtoConverter<Drums, DrumsDto> {

    @Override
    public DrumsDto convert(final Drums source) {
        final DrumsDto dto = new DrumsDto();

        dto.setId(source.getId());
        dto.setColor(source.getColor());
        dto.setDecibels(source.getDecibels());
        dto.setManufacturer(source.getManufacturer());
        dto.setVersion(source.getVersion());

        return dto;
    }
}

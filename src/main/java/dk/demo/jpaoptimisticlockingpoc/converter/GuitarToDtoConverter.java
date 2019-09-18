package dk.demo.jpaoptimisticlockingpoc.converter;

import dk.demo.jpaoptimisticlockingpoc.dto.GuitarDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Guitar;
import org.springframework.stereotype.Service;

@Service
public class GuitarToDtoConverter extends AbstractToDtoConverter<Guitar, GuitarDto> {
    public GuitarDto convert(final Guitar source) {
        final GuitarDto dto = new GuitarDto();

        dto.setId(source.getId());
        dto.setYear(source.getYear());
        dto.setColor(source.getColor());
        dto.setType(source.getType());
        dto.setVersion(source.getVersion());

        return dto;
    }
}

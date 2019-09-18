package dk.demo.jpaoptimisticlockingpoc.converter;

abstract public class AbstractFromDtoConverter <DTO, ENTITY> {

    abstract public void convert(final DTO dto, final ENTITY entity);

}

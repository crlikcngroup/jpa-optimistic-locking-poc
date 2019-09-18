package dk.demo.jpaoptimisticlockingpoc.converter;

abstract public class AbstractToDtoConverter <FROM, TO> {

    abstract public TO convert(final FROM source);

}

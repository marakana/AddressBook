package addressbook;

@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.METHOD })
public @interface Default {
    public abstract String value();
}

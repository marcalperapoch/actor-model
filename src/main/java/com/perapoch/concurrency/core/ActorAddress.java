package com.perapoch.concurrency.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class ActorAddress {

    private static final ActorAddress ROOT = ActorAddress.of("/");
    private static final ActorAddress ANONYMOUS = ActorAddress.of("/anonymous");

    private final Path path;
    private final String name;

    private ActorAddress(Path path) {
        final int totalNames = path.getNameCount();
        this.name = (totalNames > 0) ? path.getName(totalNames - 1).toString() : "root";
        this.path = path;
    }

    private boolean isRoot() {
        return this == ROOT;
    }

    private boolean isAnonymous() {
        return this == ANONYMOUS;
    }

    private boolean hasParent() {
        return !isRoot() && !isAnonymous();
    }

    public Optional<ActorAddress> getParentAddress() {
        if (hasParent()) {
            return Optional.of(ActorAddress.of(path.getParent()));
        }
        return Optional.empty();
    }

    private Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorAddress that = (ActorAddress) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "ActorAddress{" +
                "path=" + path +
                ", name='" + name + '\'' +
                '}';
    }

    private static ActorAddress of(String path) {
        return of(Paths.get(path));
    }

    private static ActorAddress of(Path path) {
        return new ActorAddress(path);
    }

    public static ActorAddress rootAddress() {
        return ROOT;
    }

    public static ActorAddress anonymousAddress() {
        return ANONYMOUS;
    }

    public static ActorAddress of(ActorAddress parent, String childName) {
        final Path newPath = parent.getPath().resolve(childName);
        return of(newPath);
    }

}

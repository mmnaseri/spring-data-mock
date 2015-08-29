/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.tools;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:15)
 */
public abstract class TypeBuilder {

    private static class DefaultParameterizedType implements ParameterizedType {

        private final Type rawType;
        private final Type[] actualTypeArguments;
        private final Type ownerType;

        private DefaultParameterizedType(Type rawType, Type[] actualTypeArguments, Type ownerType) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefaultParameterizedType that = (DefaultParameterizedType) o;
            return Arrays.equals(actualTypeArguments, that.actualTypeArguments) && !(ownerType != null ? !ownerType.equals(that.ownerType) : that.ownerType != null) && rawType.equals(that.rawType);

        }

        @Override
        public int hashCode() {
            int result = rawType.hashCode();
            result = 31 * result + (actualTypeArguments != null ? Arrays.hashCode(actualTypeArguments) : 0);
            result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
            return result;
        }
    }

    private static class DefaultGenericArrayType implements GenericArrayType {

        private final Type genericComponentType;

        private DefaultGenericArrayType(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefaultGenericArrayType that = (DefaultGenericArrayType) o;
            return genericComponentType.equals(that.genericComponentType);

        }

        @Override
        public int hashCode() {
            return genericComponentType.hashCode();
        }
    }

    public static Type parameterizedType(Type rawType, Type... arguments) {
        return new DefaultParameterizedType(rawType, arguments, null);
    }

    public static <C extends Collection<?>> Type collection(Class<C> collectionType, Type itemType) {
        return parameterizedType(collectionType, itemType);
    }

    public static Type collection(Type itemType) {
        return collection(Collection.class, itemType);
    }

    public static <M extends Map> Type map(Class<M> mapType, Type keyType, Type valueType) {
        return parameterizedType(mapType, keyType, valueType);
    }

    public static Type map(Type keyType, Type valueType) {
        return map(Map.class, keyType, valueType);
    }

    public static Type array(Type componentType) {
        return new DefaultGenericArrayType(componentType);
    }

}

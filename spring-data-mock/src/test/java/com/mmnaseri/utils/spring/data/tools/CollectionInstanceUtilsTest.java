package com.mmnaseri.utils.spring.data.tools;

import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class CollectionInstanceUtilsTest extends AbstractUtilityClassTest {

    @Override
    protected Class<?> getUtilityClass() {
        return CollectionInstanceUtils.class;
    }

    @Test
    public void testSupportedConcreteTypes() throws Exception {
        final List<? extends Class<?>> collectionTypes = Arrays.asList(HashSet.class,
                TreeSet.class,
                CopyOnWriteArraySet.class,
                LinkedHashSet.class,
                ArrayList.class,
                LinkedList.class,
                Vector.class,
                Stack.class,
                PriorityQueue.class,
                PriorityBlockingQueue.class,
                ArrayDeque.class,
                ConcurrentLinkedQueue.class,
                LinkedBlockingQueue.class,
                LinkedBlockingDeque.class);
        for (Class<?> collectionType : collectionTypes) {
            final Collection<?> collection = CollectionInstanceUtils.getCollection(collectionType);
            assertThat(collection, is(notNullValue()));
            assertThat(collection, is(instanceOf(collectionType)));
        }
    }

    @Test
    public void testSupportedAbstractTypes() throws Exception {
        assertThat(CollectionInstanceUtils.getCollection(Set.class), is(instanceOf(Set.class)));
        assertThat(CollectionInstanceUtils.getCollection(List.class), is(instanceOf(List.class)));
        assertThat(CollectionInstanceUtils.getCollection(Queue.class), is(instanceOf(Queue.class)));
        assertThat(CollectionInstanceUtils.getCollection(Deque.class), is(instanceOf(Deque.class)));
        assertThat(CollectionInstanceUtils.getCollection(Collection.class), is(instanceOf(Collection.class)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnknownType() throws Exception {
        CollectionInstanceUtils.getCollection(Class.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullValue() throws Exception {
        CollectionInstanceUtils.getCollection(null);
    }

}
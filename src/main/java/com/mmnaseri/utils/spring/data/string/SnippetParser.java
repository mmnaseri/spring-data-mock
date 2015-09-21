package com.mmnaseri.utils.spring.data.string;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface SnippetParser<E> {

    /**
     * This method will be called by an instance of {@link DocumentReader} through the
     * {@link DocumentReader#parse(SnippetParser)} method
     * @param reader the reader which holds the current document
     * @return the result of the parse operation. This usually is some sort of data
     * represented by the text just read.
     */
    E parse(DocumentReader reader);

}

/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *  * or more contributor license agreements.  See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership.  The ASF licenses this file
 *  * to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.apache.tinkerpop.gremlin.structure.util;

import org.apache.tinkerpop.gremlin.structure.P;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class ConjunctionP<V> extends P<V> {

    protected List<P<V>> predicates;

    public ConjunctionP(final P<V> predicate, final P<V>... predicates) {
        super(null, null);
        this.predicates = new ArrayList<>();
        this.predicates.add(predicate);
        for (final P<V> p : predicates) {
            this.predicates.add(p);
        }
    }

    public List<P<V>> getPredicates() {
        return Collections.unmodifiableList(this.predicates);
    }

    @Override
    public P<V> negate() {
        final List<P<V>> negated = new ArrayList<>();
        for (final P<V> predicate : this.predicates) {
            negated.add(predicate.negate());
        }
        this.predicates = negated;
        return this;
    }

    @Override
    public ConjunctionP<V> clone() {
        final ConjunctionP<V> clone = (ConjunctionP<V>) super.clone();
        clone.predicates = new ArrayList<>();
        for (final P<V> p : this.predicates) {
            clone.predicates.add(p.clone());
        }
        return clone;
    }

    @Override
    public P<V> and(final Predicate<? super V> predicate) {
        if (!(predicate instanceof P))
            throw new IllegalArgumentException("Only P predicates can be and'd together");
        return new AndP<V>(this, (P<V>) predicate);
    }

    @Override
    public P<V> or(final Predicate<? super V> predicate) {
        if (!(predicate instanceof P))
            throw new IllegalArgumentException("Only P predicates can be or'd together");
        return new OrP<V>(this, (P<V>) predicate);
    }
}
/*
 * Copyright 2017 Mike Limansky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.limansky.beanpuree

import shapeless.{HList, LabelledGeneric}

trait BeanConverter[B, S] {
  def from(s: S): B
  def to(b: B): S
}

object BeanConverter {
  def apply[B, S](implicit beanConverter: BeanConverter[B, S]): BeanConverter[B, S] = beanConverter

  implicit def converter[B, S, R <: HList](implicit
    lgen: LabelledGeneric.Aux[S, R],
    bgen: BeanGeneric.Aux[B, R]
  ): BeanConverter[B, S] = new BeanConverter[B, S] {
    override def from(s: S): B = bgen.from(lgen.to(s))
    override def to(b: B): S = lgen.from(bgen.to(b))
  }
}

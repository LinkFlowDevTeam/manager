/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.test32.common.wrapper.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.test32.common.wrapper.google.common.annotations.GwtCompatible;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@GwtCompatible
public final class Preconditions {
  private Preconditions() {}

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * @param expression a boolean expression
   * @throws IllegalArgumentException if {@code expression} is false
   */
  public static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * @param expression a boolean expression
   * @param errorMessage the exception message to use if the check fails; will be converted to a
   *     string using {@link String#valueOf(Object)}
   * @throws IllegalArgumentException if {@code expression} is false
   */
  public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(errorMessage));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * @param expression a boolean expression
   * @param errorMessageTemplate a template for the exception message should the check fail. The
   *     message is formed by replacing each {@code %s} placeholder in the template with an
   *     argument. These are matched by position - the first {@code %s} gets {@code
   *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
   *     square braces. Unmatched placeholders will be left as-is.
   * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
   *     are converted to strings using {@link String#valueOf(Object)}.
   * @throws IllegalArgumentException if {@code expression} is false
   */
  public static void checkArgument(
      boolean expression,
      @Nullable String errorMessageTemplate,
      Object @Nullable ... errorMessageArgs) {
    if (!expression) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b,
      @Nullable String errorMessageTemplate,
      @Nullable Object p1,
      @Nullable Object p2,
      @Nullable Object p3) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   *
   * <p>See {@link #checkArgument(boolean, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  public static void checkArgument(
      boolean b,
      @Nullable String errorMessageTemplate,
      @Nullable Object p1,
      @Nullable Object p2,
      @Nullable Object p3,
      @Nullable Object p4) {
    if (!b) {
      throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
    }
  }

  public static void checkState(boolean expression, @Nullable Object errorMessage) {
    if (!expression) {
      throw new IllegalStateException(String.valueOf(errorMessage));
    }
  }


//  public static void checkState(boolean expression) {
//    if (!expression) {
//      throw new IllegalStateException();
//    }
//  }

//  public static void checkState(
//      boolean expression,
//      @Nullable String errorMessageTemplate,
//      @Nullable Object @Nullable ... errorMessageArgs) {
//    if (!expression) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b,
//      @Nullable String errorMessageTemplate,
//      @Nullable Object p1,
//      @Nullable Object p2,
//      @Nullable Object p3) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
//    }
//  }
//
//  /**
//   * Ensures the truth of an expression involving the state of the calling instance, but not
//   * involving any parameters to the calling method.
//   *
//   * <p>See {@link #checkState(boolean, String, Object...)} for details.
//   *
//   * @since 20.0 (varargs overload since 2.0)
//   */
//  public static void checkState(
//      boolean b,
//      @Nullable String errorMessageTemplate,
//      @Nullable Object p1,
//      @Nullable Object p2,
//      @Nullable Object p3,
//      @Nullable Object p4) {
//    if (!b) {
//      throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
//    }
//  }

  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T reference, @Nullable Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T reference, @Nullable String errorMessageTemplate, Object @Nullable ... errorMessageArgs) {
    if (reference == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
    }
    return reference;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, char p1) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, int p1) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, long p1) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, @Nullable Object p1) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, char p1, char p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, char p1, int p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, char p1, long p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, int p1, char p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, int p1, int p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, int p1, long p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, long p1, char p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, long p1, int p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, long p1, long p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj,
      @Nullable String errorMessageTemplate,
      @Nullable Object p1,
      @Nullable Object p2,
      @Nullable Object p3) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
    }
    return obj;
  }

  /**
   * Ensures that an object reference passed as a parameter to the calling method is not null.
   *
   * <p>See {@link #checkNotNull(Object, String, Object...)} for details.
   *
   * @since 20.0 (varargs overload since 2.0)
   */
  @CanIgnoreReturnValue
  public static <T extends @NonNull Object> T checkNotNull(
      T obj,
      @Nullable String errorMessageTemplate,
      @Nullable Object p1,
      @Nullable Object p2,
      @Nullable Object p3,
      @Nullable Object p4) {
    if (obj == null) {
      throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
    }
    return obj;
  }

  /*
   * All recent hotspots (as of 2009) *really* like to have the natural code
   *
   * if (guardExpression) {
   *    throw new BadException(messageExpression);
   * }
   *
   * refactored so that messageExpression is moved to a separate String-returning method.
   *
   * if (guardExpression) {
   *    throw new BadException(badMsg(...));
   * }
   *
   * The alternative natural refactorings into void or Exception-returning methods are much slower.
   * This is a big deal - we're talking factors of 2-8 in microbenchmarks, not just 10-20%. (This is
   * a hotspot optimizer bug, which should be fixed, but that's a separate, big project).
   *
   * The coding pattern above is heavily used in java.util, e.g. in ArrayList. There is a
   * RangeCheckMicroBenchmark in the JDK that was used to test this.
   *
   * But the methods in this class want to throw different exceptions, depending on the args, so it
   * appears that this pattern is not directly applicable. But we can use the ridiculous, devious
   * trick of throwing an exception in the middle of the construction of another exception. Hotspot
   * is fine with that.
   */

  /**
   * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
   * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
   *
   * @param index a user-supplied index identifying an element of an array, list or string
   * @param size the size of that array, list or string
   * @return the value of {@code index}
   * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
   * @throws IllegalArgumentException if {@code size} is negative
   */
  @CanIgnoreReturnValue
  public static int checkElementIndex(int index, int size) {
    return checkElementIndex(index, size, "index");
  }

  /**
   * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
   * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
   *
   * @param index a user-supplied index identifying an element of an array, list or string
   * @param size the size of that array, list or string
   * @param desc the text to use to describe this index in an error message
   * @return the value of {@code index}
   * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
   * @throws IllegalArgumentException if {@code size} is negative
   */
  @CanIgnoreReturnValue
  public static int checkElementIndex(int index, int size, @Nullable String desc) {
    // Carefully optimized for execution by hotspot (explanatory comment above)
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
    }
    return index;
  }

  private static String badElementIndex(int index, int size, @Nullable String desc) {
    if (index < 0) {
      return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
    } else if (size < 0) {
      throw new IllegalArgumentException("negative size: " + size);
    } else { // index >= size
      return Strings.lenientFormat("%s (%s) must be less than size (%s)", desc, index, size);
    }
  }

  /**
   * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
   * size {@code size}. A position index may range from zero to {@code size}, inclusive.
   *
   * @param index a user-supplied index identifying a position in an array, list or string
   * @param size the size of that array, list or string
   * @return the value of {@code index}
   * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
   * @throws IllegalArgumentException if {@code size} is negative
   */
//  @CanIgnoreReturnValue
//  public static int checkPositionIndex(int index, int size) {
//    return checkPositionIndex(index, size, "index");
//  }

  /**
   * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
   * size {@code size}. A position index may range from zero to {@code size}, inclusive.
   *
   * @param index a user-supplied index identifying a position in an array, list or string
   * @param size the size of that array, list or string
   * @param desc the text to use to describe this index in an error message
   * @return the value of {@code index}
   * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
   * @throws IllegalArgumentException if {@code size} is negative
   */
//  @CanIgnoreReturnValue
//  public static int checkPositionIndex(int index, int size, @Nullable String desc) {
//    // Carefully optimized for execution by hotspot (explanatory comment above)
//    if (index < 0 || index > size) {
//      throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
//    }
//    return index;
//  }

  private static String badPositionIndex(int index, int size, @Nullable String desc) {
    if (index < 0) {
      return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
    } else if (size < 0) {
      throw new IllegalArgumentException("negative size: " + size);
    } else { // index > size
      return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", desc, index, size);
    }
  }

  /**
   * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in an array, list
   * or string of size {@code size}, and are in order. A position index may range from zero to
   * {@code size}, inclusive.
   *
   * @param start a user-supplied index identifying a starting position in an array, list or string
   * @param end a user-supplied index identifying a ending position in an array, list or string
   * @param size the size of that array, list or string
   * @throws IndexOutOfBoundsException if either index is negative or is greater than {@code size},
   *     or if {@code end} is less than {@code start}
   * @throws IllegalArgumentException if {@code size} is negative
   */
  public static void checkPositionIndexes(int start, int end, int size) {
    // Carefully optimized for execution by hotspot (explanatory comment above)
    if (start < 0 || end < start || end > size) {
      throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
    }
  }

  private static String badPositionIndexes(int start, int end, int size) {
    if (start < 0 || start > size) {
      return badPositionIndex(start, size, "start index");
    }
    if (end < 0 || end > size) {
      return badPositionIndex(end, size, "end index");
    }
    // end < start
    return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", end, start);
  }
}

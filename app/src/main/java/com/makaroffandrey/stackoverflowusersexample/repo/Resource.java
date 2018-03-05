/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makaroffandrey.stackoverflowusersexample.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.makaroffandrey.stackoverflowusersexample.repo.Status.ERROR;
import static com.makaroffandrey.stackoverflowusersexample.repo.Status.LOADING;
import static com.makaroffandrey.stackoverflowusersexample.repo.Status.SUCCESS;

/**
 * A generic class that holds a value with its loading status.
 * Shamelessly borrowed from <a href="https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.java">here</a>
 *
 * @param <T>
 */
public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final Throwable throwable;

    @Nullable
    public final T data;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable Throwable throwable) {
        this.status = status;
        this.data = data;
        this.throwable = throwable;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable exception, @Nullable T data) {
        return new Resource<>(ERROR, data, exception);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (throwable != null ? !throwable.equals(resource.throwable) : resource.throwable != null) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", throwable='" + throwable + '\'' +
                ", data=" + data +
                '}';
    }
}

/*
 * Copyright (c) 2015. Dmitriy Manzhosov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tac.cache;

import android.net.Uri;

import com.tac.cache.resolver.DataResolverDao;

import java.util.HashMap;

public class DataResolverMap {

    private static HashMap<Uri, DataResolverDao> URLDataResolverMap = new HashMap<Uri, DataResolverDao>();

    public static void addUriResolver(Uri uri, DataResolverDao resolver) {
        URLDataResolverMap.put(uri, resolver);
    }

    public static DataResolverDao getUriResolver(Uri uri) {
        return URLDataResolverMap.get(uri);
    }
}
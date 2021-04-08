/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.gradle.plugin.internal.dsl

import groovy.transform.CompileStatic
import org.gradle.api.internal.provider.Providers
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.jreleaser.gradle.plugin.dsl.PackagerTool
import org.jreleaser.model.Tool

import javax.inject.Inject

/**
 *
 * @author Andres Almiray
 * @since 0.1.0
 */
@CompileStatic
abstract class AbstractPackagerTool implements PackagerTool {
    final Property<Boolean> enabled
    final MapProperty<String, String> extraProperties

    @Inject
    AbstractPackagerTool(ObjectFactory objects) {
        enabled = objects.property(Boolean).convention(Providers.notDefined())
        extraProperties = objects.mapProperty(String, String).convention(Providers.notDefined())
    }

    protected abstract String toolName()

    @Internal
    boolean isSet() {
        enabled.present ||
            extraProperties.present
    }

    protected <T extends Tool> void fillToolProperties(T tool) {
        tool.enabled = enabled.orElse(isSet())
        if (extraProperties.present) tool.extraProperties.putAll(extraProperties.get())
    }
}

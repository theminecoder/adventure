/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/* package */ final class TitleSerializer implements TypeSerializer<Title> {
  static final TypeToken<Title> TYPE = TypeToken.of(Title.class);
  static final TitleSerializer INSTANCE = new TitleSerializer();
  static final String TITLE = "title";
  static final String SUBTITLE = "subtitle";
  static final String FADE_IN = "fade-in";
  static final String STAY = "stay";
  static final String FADE_OUT = "fade-out";

  @Override
  public @Nullable Title deserialize(@NonNull final TypeToken<?> type, @NonNull final ConfigurationNode value) throws ObjectMappingException {
    if(value.isEmpty()) {
      return null;
    }
    final Component title = value.getNode(TITLE).getValue(ComponentTypeSerializer.TYPE, TextComponent.empty());
    final Component subtitle = value.getNode(SUBTITLE).getValue(ComponentTypeSerializer.TYPE, TextComponent.empty());
    final Duration fadeIn = value.getNode(FADE_IN).getValue(DurationSerializer.INSTANCE.type(), Title.KEEP);
    final Duration stay = value.getNode(STAY).getValue(DurationSerializer.INSTANCE.type(), Title.KEEP);
    final Duration fadeOut = value.getNode(FADE_OUT).getValue(DurationSerializer.INSTANCE.type(), Title.KEEP);
    return Title.of(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  public void serialize(@NonNull final TypeToken<?> type, @Nullable final Title obj, @NonNull final ConfigurationNode value) throws ObjectMappingException {
    if(obj == null) {
      value.setValue(null);
      return;
    }

    value.getNode(TITLE).setValue(ComponentTypeSerializer.TYPE, obj.title());
    value.getNode(SUBTITLE).setValue(ComponentTypeSerializer.TYPE, obj.subtitle());
    value.getNode(FADE_IN).setValue(DurationSerializer.INSTANCE.type(), obj.fadeInTime());
    value.getNode(STAY).setValue(DurationSerializer.INSTANCE.type(), obj.stayTime());
    value.getNode(FADE_OUT).setValue(DurationSerializer.INSTANCE.type(), obj.fadeOutTime());
  }
}

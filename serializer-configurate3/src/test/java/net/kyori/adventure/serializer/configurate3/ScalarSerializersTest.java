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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScalarSerializersTest {

  @Test
  void testSerializeNamedTextColor() throws ObjectMappingException {
    final ConfigurationNode node = this.node();
    node.setValue(TypeToken.of(TextColor.class), NamedTextColor.AQUA);
    assertEquals("aqua", node.getValue());

    node.setValue("dark_purple");
    assertEquals(NamedTextColor.DARK_PURPLE, node.getValue(TextColorSerializer.INSTANCE.type()));
  }

  @Test
  void testSerializeHexTextColor() throws ObjectMappingException {
    // read as hex string
    final ConfigurationNode node = this.node("#adface");
    assertEquals(TextColor.of(0xadface), node.getValue(TypeToken.of(TextColor.class)));

    // read as int
    node.setValue(0x2468AC);
    assertEquals(TextColor.of(0x2468AC), node.getValue(TypeToken.of(TextColor.class)));

    node.setValue(TypeToken.of(TextColor.class), TextColor.of(0x123456));
    assertEquals("#123456", node.getValue());
  }

  @Test
  void testSerializerKey() throws ObjectMappingException {
    final TypeToken<Key> keyType = TypeToken.of(Key.class);
    assertThrows(ObjectMappingException.class, () -> {
      this.node("MineCRaft:test/namespace-invalid.gif").getValue(keyType);
    }, "namespace");
    assertThrows(ObjectMappingException.class, () -> {
      this.node("minecraft:test path invalid.gif").getValue(keyType);
    }, "path");
    assertEquals(Key.of("test/valid.wav"), this.node("minecraft:test/valid.wav").getValue(keyType));
  }

  @Test
  void testSerializeKeyCustomNamespace() throws ObjectMappingException {
    final TypeToken<Key> keyType = TypeToken.of(Key.class);
    assertEquals(Key.of("adventure", "meow"), this.node("adventure:meow").getValue(keyType));
  }

  private static final ConfigurationOptions OPTIONS = ConfigurationOptions.defaults()
    .withSerializers(s -> ConfigurateComponentSerializer.builder().scalarSerializer(GsonComponentSerializer.gson()).outputStringComponents(true).build().populate(s))
    .withNativeTypes(ImmutableSet.of(String.class, Integer.class, Boolean.class, Double.class));

  private ConfigurationNode node() {
    return ConfigurationNode.root(OPTIONS);
  }

  private ConfigurationNode node(final Object value) {
    return ConfigurationNode.root(OPTIONS).setValue(value);
  }
}

# Experience Source
## Harvesting Crops
Hooks into the method which gets the items from the loot table

Condition
- `tool` / `tool_tag` / `tool_nbt` (Tool used)
- `block` / `block_tag` / `block_nbt` (Block broken)

Parameters
- `dropped_crops`: Amount of dropped crops (incl. stack size) (`#forge:crops`)
- `dropped_seeeds`: Amount of dropped seeds (incl. stack size) (`#forge:seeds`)

Example:
```json
{
  "type": "pufferfish_unofficial_additions:harvest_crops",
  "data": {
    "conditions": {
      "crops": {
        "type": "block_tag",
        "data": {
          "tag": "minecraft:crops"
        }
      }
    },
    "parameters": {
      "dropped_crops": {
        "type": "dropped_crops"
      },
      "dropped_seeds": {
        "type": "dropped_seeds"
      }
    },
    "experience": [
      {
        "condition": "crops",
        "expression": "dropped_crops + (dropped_seeds * 0.1)"
      }
    ]
  }
}
```

## Fishing Experience
Gets called per fished item

Conditions
- `tool` / `tool_tag` / `tool_nbt` (Tool used)
- `fished` / `fished_tag` / `fished_nbt` (Item fished)

Parameters
- `fished_amount`: Stack size of the fished item

Example:
```json
{
  "type": "pufferfish_unofficial_additions:fishing",
  "data": {
    "conditions": {
      "fished": {
        "type": "fished_tag",
        "data": {
          "tag": "minecraft:fishes"
        }
      }
    },
    "parameters": {
      "fished_amount": {
        "type": "fished_amount"
      }
    },
    "experience": [
      {
        "condition": "fished",
        "expression": "fished_amount * 3"
      }
    ]
  }
}
```

# Rewards
## Walkable Powder Snow
Allows walking on powder snow

```json
{
  "type": "puffish_skills:tag",
  "data": {
    "tag": "walk_on_powder_snow"
  }
}
```

## Enchantment Attributes
- Looting
- Respiration
- Fishing Lure
- Fishing Luck

The base value for these is the enchantment value which gets checked
- Meaning there won't be any benefits using `multiply_base` / `multiply_total` if you don't have the enchantment
- `addition` will be affected by `multiply_total`

## Harvest Attribute
Can be used to increase the amount of harvested items
- `addition` of `1` means one additional crop (not seed)
- The base value is the initial harvested amount

## Iron's Spells 'n Spellbooks
Added attributes to increase (or lower) spell levels
- General attribute (meaning all spells) (`pufferfish_unofficial_additions:spell_general`)
- Attribute for each school type (`pufferfish_unofficial_additions:spell_school_<...>`)
- Attribute for each spell type (`pufferfish_unofficial_additions:spell_type_<...>`)

How it works:
- The base value of these attributes is the level of the spell being used - meaning `modify_base` will always have an effect
- Spells with a maximum level of `1` will not be affected, since there usually is no reason to do so
- The three attribute types function as one - their modifiers are gathered together before calculation
- There is no rounding - meaning a spell level of `1.75` will result in `1`

Examples:
```json
{
  "spell_mastery_keystone": {
    "title": "Spell Mastery",
    "description": "Increases the level of your spells by 40%",
    "frame": {
      "type": "advancement",
      "data": {
        "frame": "challenge"
      }
    },
    "icon": {
      "type": "effect",
      "data": {
        "effect": "minecraft:glowing"
      }
    },
    "rewards": [
      {
        "type": "puffish_skills:attribute",
        "data": {
          "attribute": "pufferfish_unofficial_additions:spell_general",
          "value": 0.4,
          "operation": "multiply_total"
        }
      }
    ]
  },
  "blood_slash_major": {
    "title": "Blood Slash Mastery",
    "description": "Increases the level of [Blood Slash] by 1",
    "frame": {
      "type": "advancement",
      "data": {
        "frame": "challenge"
      }
    },
    "icon": {
      "type": "item",
      "data": {
        "item": "irons_spellbooks:scroll"
      }
    },
    "rewards": [
      {
        "type": "puffish_skills:attribute",
        "data": {
          "attribute": "pufferfish_unofficial_additions:spell_type_blood_slash",
          "value": 1,
          "operation": "addition"
        }
      }
    ]
  },
  "fire_school_mastery": {
    "title": "Fire School Mastery",
    "description": "Increases the level of [Fire] spells by 1",
    "frame": {
      "type": "advancement",
      "data": {
        "frame": "challenge"
      }
    },
    "icon": {
      "type": "item",
      "data": {
        "item": "irons_spellbooks:fire_rune"
      }
    },
    "rewards": [
      {
        "type": "puffish_skills:attribute",
        "data": {
          "attribute": "pufferfish_unofficial_additions:spell_school_fire",
          "value": 1,
          "operation": "addition"
        }
      }
    ]
  }
}
```

---

Added an attribute to potentially not use up a scroll
- `pufferfish_unofficial_additions:keep_scroll`
- Value between `0` (`0%` chance) and 1 (`100%` chance)
- Using `multiply_base` will have no effect since the base value is `0`
- Using `multiply_total` without prior `addition` will also have no effect

Example:
```json
{
  "magic_root": {
    "title": "Experienced Magician",
    "description": "50% Chance to keep your Scroll after casting",
    "frame": {
      "type": "advancement",
      "data": {
        "frame": "challenge"
      }
    },
    "icon": {
      "type": "item",
      "data": {
        "item": "irons_spellbooks:scroll"
      }
    },
    "rewards": [
      {
        "type": "puffish_skills:attribute",
        "data": {
          "attribute": "pufferfish_unofficial_additions:keep_scroll",
          "value": 0.5,
          "operation": "addition"
        }
      }
    ]
  }
}
```

### School Types
See https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SchoolRegistry.java

### Spell Types
See https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java
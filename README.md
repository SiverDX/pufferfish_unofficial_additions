# General
Addon to https://www.curseforge.com/minecraft/mc-mods/puffish-skills

Contains support for
- https://www.curseforge.com/minecraft/mc-mods/irons-spells-n-spellbooks

---

Attributes have been moved to [Additional Attributes](https://www.curseforge.com/minecraft/mc-mods/additional-attributes)
- This mod will try to update attributes applied to entities, item stacks and definitions for the skill tree to the `additional_attributes` mod
  - The skill tree files themselves will not be updated, it will simply substitute the namespace when trying to load the attribute

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

# Iron's Spells 'n Spellbooks
## Experience Sources
Added an experience source for casting spells

**Note**: The experience source will trigger for each spell tick, meaning for continous spells it can happen multiple times (see the `expected_ticks` parameter)

**Parameters**:
- `level` / `min_level` / `max_level` belong to the spell being cast
- `min_level_rarity` is the min. level for the rarity (could be 4 instead of 1 if you're using a rare spell)
- `rarity` is the ordinal of the rarity (0 is COMMON and 4 is LEGENDARY)
- `mana_cost` is the mana cost for the spell at that level (if it consumes mana)
- `mana_cost_per_second` is only set for continuous spells (and if it consumes mana)
- `cast_duration` (in seconds) is for continuous spells
- `cast_charge_time` (in seconds) is for long spells (the charge-up time)
- `cooldown` (in seconds)
- `expected_ticks` will be 1 for instant and long type spells - for continous it's the amount of spell ticks if the whole duration is used

**Conditions**:
- `item` / `item_nbt` / `item_tag` is the main hand item when the spell is cast
- `spellbook` / `spellbook_nbt` / `spellbook_tag` is the spellbook in the curios slot
- `school_type` is the school type of the spell (link for all ids at the bottom)
    - `data` entry is `value`
- `spell_id` is the spell being cast (link for all ids at the bottom)
    - `data` entry is `value`
- `cast_type`: INSTANT | LONG | CONTINUOUS
    - `data` entry is `value`
- `spell_rarity_name`: COMMON | UNCOMMON | RARE | EPIC | LEGENDARY
    - `data` entry is `value`

Example:
```json
{
  "type": "pufferfish_unofficial_additions:spell_casting",
  "data": {
    "parameters": {
      "level": {
        "type": "level"
      },
      "min_level": {
        "type": "min_level"
      },
      "max_level": {
        "type": "max_level"
      },
      "rarity": {
        "type": "rarity"
      },
      "mana_cost": {
        "type": "mana_cost"
      }
    },
    "conditions": {
      "spellbook": {
        "type": "item",
        "data": {
          "item": "irons_spellbooks:iron_spell_book"
        }
      },
      "fire_school": {
        "type": "school_type",
        "data": {
          "value": "irons_spellbooks:fire"
        }
      },
      "blaze_storm": {
        "type": "spell_id",
        "data": {
          "value": "irons_spellbooks:blaze_storm"
        }
      },
      "black_hole": {
        "type": "spell_id",
        "data": {
          "value": "irons_spellbooks:black_hole"
        }
      }
    },
    "experience": [
      {
        "condition": "spellbook & !blaze_storm & fire_school",
        "expression": "level * 2"
      },
      {
        "condition": "black_hole",
        "expression": "level + (mana_cost / 10)"
      }
    ]
  }
}
```

```json
{
  "type": "pufferfish_unofficial_additions:spell_casting",
  "data": {
    "parameters": {
      "level": {
        "type": "level"
      },
      "rarity": {
        "type": "rarity"
      },
      "mana_cost": {
        "type": "mana_cost"
      },
      "mana_cost_per_second": {
        "type": "mana_cost_per_second"
      },
      "ticks": {
        "type": "expected_ticks"
      }
    },
    "conditions": {
      "multiple_ticks": {
        "type": "cast_type",
        "data": {
          "value": "CONTINUOUS"
        }
      }
    },
    "experience": [
      {
        "condition": "multiple_ticks",
        "expression": "(rarity + level + (mana_cost_per_second / 10)) / 3"
      },
      {
        "condition": "!multiple_ticks",
        "expression": "rarity + level + (mana_cost / 10)"
      }
    ]
  }
}
```
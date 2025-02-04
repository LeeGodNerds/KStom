package world.cepi.kstom.item

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.WrittenBookMeta
import world.cepi.kstom.nbt.classes.CollectionClass
import world.cepi.kstom.nbt.classes.ComplexClass
import world.cepi.kstom.nbt.classes.InterestingClass

class ItemTests : StringSpec({
    val item = item<WrittenBookMeta.Builder, WrittenBookMeta>(material = Material.WRITTEN_BOOK, amount = 5) {
        displayName = Component.text("Hey!")
        damage = 5
        unbreakable = true

        lore {
            +"Hello"
            +"<red>Minestom!</red>"
        }

        title("My first book")
        author("Notch")
        pages(Component.text("This is the first page"))

        this["complexData"] = ComplexClass(5, 4, 2, true, InterestingClass("hey", 'h'))
        this["complexListData"] = CollectionClass(5, 9, 3, listOf(4, 3))
    }.withAmount(7).and {
        displayName(Component.text("Hay!"))
    }

    "item should be mutated" {
        item.amount shouldBe 7
        item.displayName shouldBe Component.text("Hay!")
    }

    "complex data should be stored" {
        val data = ComplexClass(5, 4, 2, true, InterestingClass("hey", 'h'))
        val otherData = CollectionClass(5, 9, 3, listOf(4, 3))

        item.meta().get<ComplexClass>("complexData") shouldBe data
        item.meta().get<CollectionClass>("complexListData") shouldBe otherData
    }

    "data should return null if not found" {
        item.meta().get<ComplexClass>("weirdData").shouldBeNull()
    }
    
    "book meta author should be Notch" {
        item.meta(WrittenBookMeta::class.java).author.shouldBe("Notch")
    }

    "item lore should have 2 lines containing 'Hello' and 'Minestom!'" {
        Component.text("Hello") shouldBeIn item.lore
        Component.text("Minestom!", NamedTextColor.RED) shouldBeIn item.lore
    }
})
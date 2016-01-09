package net.ilexiconn.bookwiki.client.component;

import net.ilexiconn.bookwiki.BookWiki;
import net.ilexiconn.bookwiki.BookWikiContainer;
import net.ilexiconn.bookwiki.api.BookWikiAPI;
import net.ilexiconn.bookwiki.api.IComponent;
import net.ilexiconn.bookwiki.api.IRecipe;
import net.ilexiconn.bookwiki.client.gui.BookWikiGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class RecipeComponent extends Gui implements IComponent {
    @Override
    public char getID() {
        return 'r';
    }

    @Override
    public String init(String string, String arg, String group, BookWiki bookWiki) {
        BookWikiContainer.Recipe recipe = bookWiki.getRecipeByID(arg);
        IRecipe apiRecipe = BookWikiAPI.getRecipeByType(recipe.getType());
        if (apiRecipe != null) {
            String result = group;
            for (int i = 0; i < apiRecipe.getHeight(); i++) {
                result += "\n";
            }
            return string.replace(group, result);
        } else {
            return string;
        }
    }

    @Override
    public void render(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        BookWikiContainer.Recipe recipe = bookWiki.getRecipeByID(arg);
        IRecipe apiRecipe = BookWikiAPI.getRecipeByType(recipe.getType());
        if (apiRecipe != null) {
            apiRecipe.render(mc, bookWiki, recipe, gui, x, y, mouseX, mouseY);
        }
    }

    @Override
    public void renderTooltip(Minecraft mc, BookWiki bookWiki, String arg, BookWikiGui gui, int x, int y, int mouseX, int mouseY) {
        BookWikiContainer.Recipe recipe = bookWiki.getRecipeByID(arg);
        IRecipe apiRecipe = BookWikiAPI.getRecipeByType(recipe.getType());
        if (apiRecipe != null) {
            apiRecipe.renderTooltip(mc, bookWiki, recipe, gui, x, y, mouseX, mouseY);
        }
    }
}

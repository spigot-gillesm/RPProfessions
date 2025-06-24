package com.gilles_m.rp_professions.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StringHolder {

	@JsonProperty("no-recipes-message")
	public String noRecipesMessage = "&7You don't know any recipes";
	
	@JsonProperty("set-profession-message")
	public String setProfessionMessage = "&aYou're now a %s";
	
	@JsonProperty("set-profession-level-message")
	public String setProfessionLevelMessage = "&aYou profession level has been set to &9%d";

	@JsonProperty("get-item-message")
	public String getItemMessage = "&aYou've received %s &fx&9%d &ain your inventory";

	@JsonProperty("forget-profession-message")
	public String forgetProfessionMessage = "&aYou're no longer a %s";

	@JsonProperty("already-has-profession-message")
	public String alreadyHasProfessionMessage = "&cYou already have a profession";

	@JsonProperty("profession-level-up-message")
	public String professionLevelUpMessage = "&aYou're now a level &9%d %s";

	@JsonProperty("learn-new-recipe-message")
	public String learnNewRecipeMessage = "&aYou've learned to craft %s&a!";

	@JsonProperty("learn-new-recipe-message-generic")
	public String learnNewRecipeMessageGeneric = "&aYou've learned to craft a new item!";

	@JsonProperty("invalid-profession-message")
	public String invalidProfessionMessage = "&cYou don't have the required profession";

	@JsonProperty("invalid-profession-level-message")
	public String invalidProfessionLevelMessage = "&cYou don't have the required level to learn that recipe";

	@JsonProperty("already-taught-message")
	public String alreadyTaughtMessage = "&cYou already know this recipe";

	@JsonProperty("brewing-stand-fuel-required-message")
	public String brewingStandFuelRequiredMessage = "&aThe brewing stand must be fueled for this recipe";

	@JsonProperty("select-profession-menu-title")
	public String selectProfessionMenuTitle = "Professions";

	@JsonProperty("station")
	public String station = "&7Stations";
	
	@JsonProperty("forget-profession-button")
	public String forgetProfessionButton = "&4&lForget";

	@JsonProperty("forget-profession-button-lore")
	public List<String> forgetProfessionButtonLore = List.of("",
			"&cShift &7+ &cRight Click &7to",
			"&7forget your profession",
			"",
			"&7It is &c&lpermanent&7!");

	@JsonProperty("insight-button")
	public String insightButton = "&7Insight";

	@JsonProperty("insight-menu-title")
	public String insightMenuTitle = "Insight";

	@JsonProperty("recipes-menu-title")
	public String recipesMenuTitle = "Recipes";

	@JsonProperty("recipe-menu-title")
	public String recipeMenuTitle = "Recipe";

	@JsonProperty("brewing-recipe-menu-title")
	public String brewingRecipeMenuTitle = "Brewing Recipe";

	@JsonProperty("info-menu-title")
	public String infoMenuTitle = "Info";

	@JsonProperty("info-button")
	public String infoButton = "&9Info";

	@JsonProperty("workstations-button")
	public String workstationsButton = "&9Workstations";

	@JsonProperty("select-profession-button")
	public String selectProfessionButton = "&a&lChoose this profession";

	@JsonProperty("workstations-menu-title")
	public String workstationsMenuTitle = "Workstations";

	@JsonProperty("workstation-block-button")
	public String workstationBlockButton = "&9Blocks";

	@JsonProperty("workstation-block-entry")
	public String workstationBlockEntry = "&8- &f %s";

	@JsonProperty("workstation-fuel-button")
	public String workstationFuelButton = "&9Fuel";

	@JsonProperty("workstation-no-fuel-lore")
	public List<String> workstationNoFuelLore = List.of("",
			"&7This workstation doesn't need to be fueled");

	@JsonProperty("workstation-fuel-lore")
	public List<String> workstationFuelLore = List.of("",
			"&7This workstation needs to be",
			"&7next to a block of %s");

	@JsonProperty("workstation-tool-button")
	public String workstationToolButton = "&9Tool";

	@JsonProperty("workstation-no-tool-lore")
	public List<String> workstationNoToolLore = List.of("",
			"&7This workstation doesn't need any tool",
			"",
			"&7Shift right click to access the station");

	@JsonProperty("workstation-tool-lore")
	public List<String> workstationToolLore = List.of("",
			"&7Right click the station using:",
			"",
			"&7%s");

	@JsonProperty("crafting-help-workstation")
	public String craftingHelpWorkstation = "&9Workstation: &f%s";

	@JsonProperty("crafting-help-type")
	public String craftingHelpType = "&9Type: %s";

	@JsonProperty("crafting-help-advanced")
	public String craftingHelpAdvanced = "&fAdvanced";

	@JsonProperty("crafting-help-simple")
	public String craftingHelpSimple = "&fSimple";

	@JsonProperty("crafting-help-required-level")
	public String craftingHelpRequiredLevel = "&9Required level&7: &f%d";

	@JsonProperty("crafting-help-level-cap")
	public String craftingHelpLevelCap = "&9Level cap&7: &f%d";

	@JsonProperty("crafting-help-level-gain")
	public String craftingHelpLevelGain = "&9Level gain&7: &f%d";

	@JsonProperty("allow-multiple")
	public String allowMultiple = "&9Multiple at once&7: %s";

	@JsonProperty("allow-multiple-yes")
	public String allowMultipleYes = "&fYes";

	@JsonProperty("allow-multiple-no")
	public String allowMultipleNo = "&fNo";

	@JsonProperty("workbench-display-name")
	public String workbenchDisplayName = "&fCrafting Table (regular)";

	@JsonProperty("anvil-menu-title")
	public String anvilMenuTitle = "Anvil";
	
	@JsonProperty("anvil-craft-button")
	public String anvilCraftButton = "&f&lForge";

	@JsonProperty("crafting-menu-title")
	public String craftingMenuTitle = "Crafting Station";

	@JsonProperty("crafting-craft-button")
	public String craftingCraftButton = "&a&lCraft";

	@JsonProperty("enchantment-menu-title")
	public String enchantmentMenuTitle = "Enchanting Table";
	
	@JsonProperty("enchantment-craft-button")
	public String enchantmentCraftButton = "&d&lEnchant";

	@JsonProperty("forge-menu-title")
	public String forgeMenuTitle = "Forge";

	@JsonProperty("forge-craft-button")
	public String forgeCraftButton = "&4&lSmelt";

	@JsonProperty("forge-heat-up-button")
	public String forgeHeatUpButton = "&cHeat Up";

	@JsonProperty("forge-cool-down-button")
	public String forgeCoolDownButton = "&bCool Down";

	@JsonProperty("lectern-menu-title")
	public String lecternMenuTitle = "Lectern";
	
	@JsonProperty("lectern-craft-button")
	public String lecternCraftButton = "&e&lBless";
	
	@JsonProperty("potion-menu-title")
	public String potionMenuTitle = "Brewing Stand";

	@JsonProperty("potion-craft-button")
	public String potionCraftButton = "&5&lBrew";

	@JsonProperty("show-recipe-button")
	public String showRecipeButton = "&9Recipes";

	@JsonProperty("craft-failed-button")
	public String craftFailedButton = "&cFailure";

}

package acme.features.inventor.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.items.Item;
import acme.features.inventor.moneyExchange.InventorMoneyExchangePerform;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;



@Service
public class InventorItemShowService implements AbstractShowService<Inventor, Item>{
	
	@Autowired
	protected InventorItemRepository repository;

	@Override
	public boolean authorise(final Request<Item> request) {
		assert request != null;
		
		return true;
	}

	@Override
	public Item findOne(final Request<Item> request) {
		assert request != null;
		
		final int id = request.getModel().getInteger("id");
		
		return this.repository.findItemById(id);
	}
	public Money getInternationalizedMoney(final int id) {
		
		final Money retailPrice = this.repository.findRetailPriceByItemId(id);
		return InventorMoneyExchangePerform.computeMoneyExchange(retailPrice, "EUR");
		
	}

	@Override
	public void unbind(final Request<Item> request, final Item entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		final int id = request.getModel().getInteger("id");
		final Money retailPrice = this.getInternationalizedMoney(id);

		request.unbind(entity, model, "name", "code", "technology", "description", "info", "type");
		model.setAttribute("retailPrice", retailPrice);
	}

	

}

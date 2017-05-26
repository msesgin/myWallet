import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Currency } from './currency.model';
import { CurrencyService } from './currency.service';

@Component({
    selector: 'jhi-currency-detail',
    templateUrl: './currency-detail.component.html'
})
export class CurrencyDetailComponent implements OnInit, OnDestroy {

    currency: Currency;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private currencyService: CurrencyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCurrencies();
    }

    load(id) {
        this.currencyService.find(id).subscribe((currency) => {
            this.currency = currency;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCurrencies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'currencyListModification',
            (response) => this.load(this.currency.id)
        );
    }
}

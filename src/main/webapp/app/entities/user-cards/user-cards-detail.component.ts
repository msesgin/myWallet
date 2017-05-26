import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { UserCards } from './user-cards.model';
import { UserCardsService } from './user-cards.service';

@Component({
    selector: 'jhi-user-cards-detail',
    templateUrl: './user-cards-detail.component.html'
})
export class UserCardsDetailComponent implements OnInit, OnDestroy {

    userCards: UserCards;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private userCardsService: UserCardsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserCards();
    }

    load(id) {
        this.userCardsService.find(id).subscribe((userCards) => {
            this.userCards = userCards;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserCards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userCardsListModification',
            (response) => this.load(this.userCards.id)
        );
    }
}

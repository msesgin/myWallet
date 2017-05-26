import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CardTransactions } from './card-transactions.model';
import { CardTransactionsPopupService } from './card-transactions-popup.service';
import { CardTransactionsService } from './card-transactions.service';
import { UserCards, UserCardsService } from '../user-cards';

@Component({
    selector: 'jhi-card-transactions-dialog',
    templateUrl: './card-transactions-dialog.component.html'
})
export class CardTransactionsDialogComponent implements OnInit {

    cardTransactions: CardTransactions;
    authorities: any[];
    isSaving: boolean;

    usercards: UserCards[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private cardTransactionsService: CardTransactionsService,
        private userCardsService: UserCardsService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userCardsService.query().subscribe(
            (res: Response) => { this.usercards = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cardTransactions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cardTransactionsService.update(this.cardTransactions));
        } else {
            this.subscribeToSaveResponse(
                this.cardTransactionsService.create(this.cardTransactions));
        }
    }

    private subscribeToSaveResponse(result: Observable<CardTransactions>) {
        result.subscribe((res: CardTransactions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CardTransactions) {
        this.eventManager.broadcast({ name: 'cardTransactionsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserCardsById(index: number, item: UserCards) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-card-transactions-popup',
    template: ''
})
export class CardTransactionsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cardTransactionsPopupService: CardTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cardTransactionsPopupService
                    .open(CardTransactionsDialogComponent, params['id']);
            } else {
                this.modalRef = this.cardTransactionsPopupService
                    .open(CardTransactionsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Currency } from './currency.model';
import { CurrencyPopupService } from './currency-popup.service';
import { CurrencyService } from './currency.service';

@Component({
    selector: 'jhi-currency-dialog',
    templateUrl: './currency-dialog.component.html'
})
export class CurrencyDialogComponent implements OnInit {

    currency: Currency;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private currencyService: CurrencyService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.currency.id !== undefined) {
            this.subscribeToSaveResponse(
                this.currencyService.update(this.currency));
        } else {
            this.subscribeToSaveResponse(
                this.currencyService.create(this.currency));
        }
    }

    private subscribeToSaveResponse(result: Observable<Currency>) {
        result.subscribe((res: Currency) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Currency) {
        this.eventManager.broadcast({ name: 'currencyListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-currency-popup',
    template: ''
})
export class CurrencyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private currencyPopupService: CurrencyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.currencyPopupService
                    .open(CurrencyDialogComponent, params['id']);
            } else {
                this.modalRef = this.currencyPopupService
                    .open(CurrencyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

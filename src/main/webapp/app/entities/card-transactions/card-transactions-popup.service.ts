import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CardTransactions } from './card-transactions.model';
import { CardTransactionsService } from './card-transactions.service';
@Injectable()
export class CardTransactionsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cardTransactionsService: CardTransactionsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.cardTransactionsService.find(id).subscribe((cardTransactions) => {
                this.cardTransactionsModalRef(component, cardTransactions);
            });
        } else {
            return this.cardTransactionsModalRef(component, new CardTransactions());
        }
    }

    cardTransactionsModalRef(component: Component, cardTransactions: CardTransactions): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cardTransactions = cardTransactions;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}

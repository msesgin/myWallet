import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { MyWalletTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CardTransactionsDetailComponent } from '../../../../../../main/webapp/app/entities/card-transactions/card-transactions-detail.component';
import { CardTransactionsService } from '../../../../../../main/webapp/app/entities/card-transactions/card-transactions.service';
import { CardTransactions } from '../../../../../../main/webapp/app/entities/card-transactions/card-transactions.model';

describe('Component Tests', () => {

    describe('CardTransactions Management Detail Component', () => {
        let comp: CardTransactionsDetailComponent;
        let fixture: ComponentFixture<CardTransactionsDetailComponent>;
        let service: CardTransactionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MyWalletTestModule],
                declarations: [CardTransactionsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CardTransactionsService,
                    EventManager
                ]
            }).overrideComponent(CardTransactionsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CardTransactionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CardTransactionsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CardTransactions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cardTransactions).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});

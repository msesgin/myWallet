import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { MyWalletTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AccountTransactionsDetailComponent } from '../../../../../../main/webapp/app/entities/account-transactions/account-transactions-detail.component';
import { AccountTransactionsService } from '../../../../../../main/webapp/app/entities/account-transactions/account-transactions.service';
import { AccountTransactions } from '../../../../../../main/webapp/app/entities/account-transactions/account-transactions.model';

describe('Component Tests', () => {

    describe('AccountTransactions Management Detail Component', () => {
        let comp: AccountTransactionsDetailComponent;
        let fixture: ComponentFixture<AccountTransactionsDetailComponent>;
        let service: AccountTransactionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MyWalletTestModule],
                declarations: [AccountTransactionsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AccountTransactionsService,
                    EventManager
                ]
            }).overrideComponent(AccountTransactionsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AccountTransactionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AccountTransactionsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AccountTransactions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.accountTransactions).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});

// import { TestBed } from '@angular/core/testing';
//
// import { BillService } from './bill.service';
// import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
// import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
// import { Bill } from '../../model/Bill';
// import { MOCK_BILL } from '../../testUtils/constants';
//
// describe('BillService', () => {
//   let service: BillService;
//   let controller: HttpTestingController;
//   let bills: Bill[] | undefined;
//   const url = '/api/bills';
//   const MOCK_BILLS: Bill[] = [MOCK_BILL];
//
//   beforeEach(() => {
//     TestBed.configureTestingModule({
//       providers: [
//         BillService,
//         provideHttpClient(withInterceptorsFromDi()),
//         provideHttpClientTesting(),
//       ],
//     });
//
//     service = TestBed.inject(BillService);
//     controller = TestBed.inject(HttpTestingController);
//   });
//
//   fit('should return a list of bills with one entry', () => {
//     expect(service).toBeTruthy();
//     // service.getBills().subscribe((response: Bill[]) => (bills = response));
//
//     const request = controller.expectOne(url);
//     expect(request.request.method).toBe('GET');
//     request.flush(MOCK_BILLS);
//
//     expect(bills?.length).toBe(1);
//     expect(bills).toEqual(MOCK_BILLS);
//     controller.verify();
//   });
// });

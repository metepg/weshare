import { TestBed } from '@angular/core/testing';

import { BillService } from './bill.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { COUNT_OF_BILLS } from '../../testUtils/constants';

describe('BillService', () => {
  let service: BillService;
  let controller: HttpTestingController;
  let billCount: number | undefined;
  const url = 'http://localhost:8080/api/bills/count';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BillService]
    })
    .compileComponents();
    service = TestBed.inject(BillService);
    controller = TestBed.inject(HttpTestingController);
  });

  fit('should be created', () => {
    expect(service).toBeTruthy();
    service.getBillCount().subscribe((count: number) => billCount = count);
    const request = controller.expectOne(url);
    request.flush(COUNT_OF_BILLS);

    expect(billCount).toEqual(COUNT_OF_BILLS);
    controller.verify();
  });
});

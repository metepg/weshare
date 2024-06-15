import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BillService } from '../services/bill/bill.service';
import { HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('AppComponent', () => {
  let service: BillService;
  beforeEach(async () => {
    service = TestBed.inject(BillService);
    await TestBed.configureTestingModule({
    declarations: [
        AppComponent
    ],
    imports: [RouterTestingModule,
        HttpClientModule],
    providers: [BillService, HttpClient, provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
}{
    declarations: [
        AppComponent
    ],
    imports: [RouterTestingModule,
        HttpClientTestingModule],
    providers: [BillService, HttpClient, provideHttpClient(withInterceptorsFromDi())]
}).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'weshare-ui'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('weshare-ui');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.content span')?.textContent).toContain('weshare-ui app is running!');
  });

  it('should use billService', () => {
    console.log(service.getBillCount());
  })
});

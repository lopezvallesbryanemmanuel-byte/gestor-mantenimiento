import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SitioList } from './sitio-list';

describe('SitioList', () => {
  let component: SitioList;
  let fixture: ComponentFixture<SitioList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SitioList],
    }).compileComponents();

    fixture = TestBed.createComponent(SitioList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

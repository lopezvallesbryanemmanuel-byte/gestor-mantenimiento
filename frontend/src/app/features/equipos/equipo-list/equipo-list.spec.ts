import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EquipoList } from './equipo-list';

describe('EquipoList', () => {
  let component: EquipoList;
  let fixture: ComponentFixture<EquipoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipoList],
    }).compileComponents();

    fixture = TestBed.createComponent(EquipoList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

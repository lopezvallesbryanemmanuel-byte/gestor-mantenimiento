import { TestBed } from '@angular/core/testing';
import { Sitio } from './sitio';

describe('Sitio', () => {
  let service: Sitio;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Sitio);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

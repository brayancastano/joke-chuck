import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJokesUser } from '../jokes-user.model';

@Component({
  selector: 'jhi-jokes-user-detail',
  templateUrl: './jokes-user-detail.component.html',
})
export class JokesUserDetailComponent implements OnInit {
  jokesUser: IJokesUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jokesUser }) => {
      this.jokesUser = jokesUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

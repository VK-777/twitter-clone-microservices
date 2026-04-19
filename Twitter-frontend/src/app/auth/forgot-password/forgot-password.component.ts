import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})

export class ForgotPasswordComponent {
  forgotForm: FormGroup;
  submitted  = false;
  isLoading  = false;
  emailSent  = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService
  ) {

    this.forgotForm = this.fb.group({
      email: ['', [
        Validators.required,
        Validators.pattern(/^[^\s@]+@[^\s@]+\.(com|org|in)$/)
      ]]
    });
  }

  get f() { return this.forgotForm.controls; }
  isInvalid(field: string): boolean {
    const c = this.f[field];
    return c.invalid && (c.dirty || c.touched);

  }

  onSubmit(): void {
    if (this.forgotForm.invalid) {
      this.forgotForm.markAllAsTouched();
      return;

    }
    this.isLoading = true;
    this.emailSent = this.forgotForm.value.email;
    this.authService.forgotPassword(this.emailSent).subscribe({
      next:  () => { this.isLoading = false; this.submitted = true; },
      error: () => { this.isLoading = false; this.submitted = true; }
      // show success regardless for security
    });

  }

}
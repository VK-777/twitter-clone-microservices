import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

function passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
  const pass    = group.get('password')?.value;
  const confirm = group.get('confirmPassword')?.value;
  return pass === confirm ? null : { passwordMismatch: true };

}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent {
  registerForm: FormGroup;
  showPassword  = false;
  isLoading     = false;
  errorMessage  = '';
  strengthPercent = 0;
  strengthColor   = '#6b7a99';
  strengthLabel   = 'Uppercase, lowercase, digit & special char required';

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router

  ) {

    this.registerForm = this.fb.group(
      {
        firstName: ['', [
          Validators.required,
          Validators.pattern(/^[A-Z][a-zA-Z]*(\s[A-Z][a-zA-Z]*)*$/)
        ]],

        email: ['', [
          Validators.required,
          Validators.pattern(/^[^\s@]+@[^\s@]+\.(com|org|in)$/)
        ]],
        password: ['', [
          Validators.required,
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,16}$/)
        ]],
        confirmPassword: ['', Validators.required]
      },
      { validators: passwordMatchValidator }
    );

    this.registerForm.get('password')!.valueChanges
      .subscribe(v => this.calcStrength(v));
  }

  get f() { return this.registerForm.controls; }
  
  isInvalid(field: string): boolean {
    const c = this.f[field];
    return c.invalid && (c.dirty || c.touched);
  }

  isValid(field: string): boolean {
    const c = this.f[field];
    return c.valid && (c.dirty || c.touched);
  }

  togglePassword() { this.showPassword = !this.showPassword; }
  calcStrength(value: string): void {

    if (!value) {
      this.strengthPercent = 0;
      this.strengthLabel   = 'Uppercase, lowercase, digit & special char required';
      this.strengthColor   = '#6b7a99';
      return;
    }

    let score = 0;
    if (value.length >= 8)            score++;
    if (/[A-Z]/.test(value))          score++;
    if (/[a-z]/.test(value))          score++;
    if (/\d/.test(value))             score++;
    if (/[^A-Za-z0-9]/.test(value))   score++;

    const colors = ['#f91880','#f91880','#ffd700','#1d9bf0','#00ba7c'];
    const labels = ['Too weak','Weak','Fair','Strong','Very strong ✓'];
    this.strengthPercent = score * 20;
    this.strengthColor   = colors[score - 1] ?? '#6b7a99';
    this.strengthLabel   = labels[score - 1] ?? '';
  }

    onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    const { firstName, email, password, confirmPassword } = this.registerForm.value;
    this.authService.register({ firstName, email, password, confirmPassword }).subscribe({
      next: () => {
        // Auto login after register
        this.authService.login({ email, password }).subscribe({
          next: () => {
            this.isLoading = false;
            this.router.navigate(['/profile-setup']);
          },
          error: () => {
            this.isLoading = false;
            this.router.navigate(['/login']);
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 200 || err.status === 201) {
          this.authService.login({ email, password }).subscribe({
            next: () => {
              this.router.navigate(['/profile-setup'])
            },
            error: () => {
              this.router.navigate(['/login'])
            }
          });
        } else {
          this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
        }
      }
    });
  }
}



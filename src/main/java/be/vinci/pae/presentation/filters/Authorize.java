package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.user.UserDTO.Role;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is a custom annotation used for binding a filter to a resource method. It is used in
 * conjunction with the AuthorizationRequestFilter to protect resources. The filter will only be
 * applied to methods annotated with @Authorize. The @Retention annotation specifies that this
 * annotation should be available at runtime.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

  /**
   * Returns an array of roles that are authorized to access the method annotated with @Authorize.
   * If no roles are specified, the method is accessible to all authenticated users.
   *
   * @return An array of roles that are authorized to access the method.
   */
  Role[] value() default {Role.ADMINISTRATIVE, Role.STUDENT, Role.TEACHER};
}
import typescriptEslint from "@typescript-eslint/eslint-plugin";
import globals from "globals";
import eslint from "@eslint/js";
import tseslint from 'typescript-eslint';
import tsParser from "@typescript-eslint/parser";
import stylistic from "@stylistic/eslint-plugin";
import angular from 'angular-eslint';


export default tseslint.config(
  {
    // Config for .ts and .spec.ts files
    plugins: {
      "@typescript-eslint": typescriptEslint,
      "@stylistic": stylistic
    },
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommendedTypeChecked,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.node,
      },
      parser: tsParser,
      ecmaVersion: 5,
      sourceType: "module",
      parserOptions: {
        project: "tsconfig.json",
      },
    },
    ignores: ["node_modules", "dist", ".vscode"],
    files: ["**/*.ts"],
    rules: {
      "@angular-eslint/prefer-signals": 'error',
      "eqeqeq": ["error", "smart"],
      "no-constructor-return": "error",
      "no-duplicate-imports": "error",
      "no-inner-declarations": "error",
      "no-promise-executor-return": "error",
      "no-self-compare": "error",
      "no-template-curly-in-string": "error",
      "no-unmodified-loop-condition": "error",
      "no-useless-assignment": "error",

      "@stylistic/array-bracket-newline": ["error", "consistent"],
      "@stylistic/array-element-newline": ["error", "consistent"],
      "@stylistic/arrow-parens": "error",
      "@stylistic/arrow-spacing": "error",
      "@stylistic/block-spacing": "error",
      "@stylistic/comma-spacing": "error",
      "@stylistic/comma-style": "error",
      "@stylistic/curly-newline": [ "error", { "multiline": true, "consistent": true } ],
      "@stylistic/function-call-argument-newline": ["error", "consistent"],
      "@stylistic/function-call-spacing": "error",
      "@stylistic/function-paren-newline": ["error", "consistent"],
      "@stylistic/indent": ["error", 2],
      "@stylistic/key-spacing": "error",
      "@stylistic/keyword-spacing": "error",
      "@stylistic/member-delimiter-style": "error",
      "@stylistic/new-parens": "error",
      "@stylistic/no-mixed-operators": "error",
      "@stylistic/no-mixed-spaces-and-tabs": "error",
      "@stylistic/no-multi-spaces": "error",
      "@stylistic/no-trailing-spaces": [ "error", { "ignoreComments": true } ],
      "@stylistic/no-whitespace-before-property": "error",
      "@stylistic/object-curly-newline": [ "error", { "multiline": true, "consistent": true } ],
      "@stylistic/semi-spacing": "error",
      "@stylistic/space-before-blocks": "error",
      "@stylistic/space-before-function-paren": [ "error", { "anonymous": "ignore", "named": "never", "asyncArrow": "always" } ],
      "@stylistic/switch-colon-spacing": "error",
      "@stylistic/type-annotation-spacing": "error",
      "@stylistic/type-generic-spacing": "error",
      "@stylistic/type-named-tuple-spacing": "error",
      "@typescript-eslint/no-explicit-any": "warn",
      "@typescript-eslint/no-confusing-void-expression": "error",
      "@typescript-eslint/no-deprecated": "warn",
      "@typescript-eslint/no-dynamic-delete": "error",
      "@typescript-eslint/no-invalid-void-type": [ "error", { "allowInGenericTypeArguments": true } ],
      "@typescript-eslint/no-meaningless-void-operator": "error",
      "@typescript-eslint/no-misused-spread": "error",
      "@typescript-eslint/no-mixed-enums": "error",
      "@typescript-eslint/no-non-null-asserted-nullish-coalescing": "error",
      "@typescript-eslint/no-unnecessary-boolean-literal-compare": "error",
      "@typescript-eslint/no-unnecessary-condition": "warn",
      "@typescript-eslint/no-unsafe-enum-comparison": "off",
      "@typescript-eslint/no-unsafe-member-access": "warn",
      "@typescript-eslint/no-unsafe-return": "warn",
      "@typescript-eslint/no-useless-constructor": "error",
      "@typescript-eslint/related-getter-setter-pairs": "error",
      "@typescript-eslint/restrict-template-expressions": "off",
      "@typescript-eslint/unbound-method": [ "error", { "ignoreStatic": true } ],

      "@angular-eslint/directive-selector": [
        "error",
        {
          "type": "attribute",
          "prefix": "app",
          "style": "camelCase"
        }
      ],
      "@angular-eslint/component-selector": [
        "error",
        {
          "type": "element",
          "prefix": "app",
          "style": "kebab-case"
        }
      ],
      "@angular-eslint/prefer-standalone": "off",
      "@angular-eslint/prefer-inject": "error"
    },
  },
  {
    ignores: [ "node_modules", "dist", ".vscode" ],
    files: ["**/*.spec.ts"],
    rules: {
      "@typescript-eslint/no-unsafe-call": "off"
    },
  },
  {
    // Config for .html files
    ignores: ["node_modules", "dist", ".vscode"],
    files: ["**/*.html"],
    extends: [
      ...angular.configs.templateRecommended,
    ],
    rules: {
      "@angular-eslint/template/no-duplicate-attributes": "error",
    },
  }
);

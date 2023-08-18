module.exports = {
  env: {
    browser: true,
    es2020: true,
    jest: true,
    node: true
  },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:prettier/recommended'
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: 11,
    sourceType: 'module'
  },
  plugins: ['@typescript-eslint', 'simple-import-sort'],
  rules: {
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    '@typescript-eslint/no-non-null-assertion': 'off',
    'simple-import-sort/imports': [
      'warn',
      {
        groups: [
          [
            '^\\.\\.(?!/?$)', //relative imports from other folder
            '^\\.\\./?$', // Parent imports. Put `..` last.
            '^\\.(?!/?$)', //relative imports from current folder
            '^\\./?$' // Other relative imports. Put same-folder imports and `.` last.
          ],
          ['^\\.(/style(s|d)?$)'] //styles
        ]
      }
    ]
  }
}

# ⌚ Relógio Binário (BCD) - Antigravity PoC

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Status](https://img.shields.io/badge/Status-Proof%20of%20Concept-orange?style=for-the-badge)

Este repositório contém um aplicativo Android de **Relógio Binário (Binary Coded Decimal)** desenvolvido inteiramente em **Kotlin** e **Jetpack Compose**.

> ⚠️ **Nota:** Este projeto é uma **Prova de Conceito (PoC)** gerada a partir do **Antigravity** (ou ferramentas de IA generativa de codificação), demonstrando a capacidade de criar interfaces complexas e lógica de estado a partir de um único prompt detalhado.

## 📱 Funcionalidades

O aplicativo foi construído para funcionar como um relógio de mesa (dock), com foco em visual neon e usabilidade noturna.

* **Lógica BCD (Binary Coded Decimal):**
    * Exibição em 6 colunas verticais (2 para Horas, 2 para Minutos, 2 para Segundos).
    * Leitura de baixo para cima seguindo a potência de 2 (1, 2, 4, 8).
* **Modo Noturno (Neon):**
    * Visual minimalista "Cyberpunk/Neon Blue" sobre fundo preto profundo (`#000000`).
    * No modo noturno, todas as legendas e textos auxiliares são ocultados, mantendo apenas os LEDs acesos.
* **Menu Suspenso Inteligente:**
    * Menu invisível ativado ao clicar nos 20% superiores da tela.
    * Alternância entre "Modo Normal" e "Modo Noturno".
    * *Auto-hide:* O menu desaparece automaticamente após 5 segundos sem interação.
* **Tela Sempre Ligada (Keep Screen On):**
    * Impede que o dispositivo hiberne enquanto o app está aberto, ideal para uso contínuo em mesas.
* **Persistência de Dados:**
    * O app memoriza a preferência do usuário (Modo Noturno vs. Normal) entre as sessões.

## 🎨 Screenshots

*(Adicione suas capturas de tela aqui. Ex: Uma do Modo Normal e outra do Modo Noturno)*

| Modo Normal | Modo Noturno |
|:---:|:---:|
| ![Normal](url_da_imagem_normal) | ![Night](url_da_imagem_night) |

## 🧠 O Prompt (Origem do Código)

O código base deste projeto foi gerado utilizando o seguinte prompt detalhado, focado em requisitos de UI e Lógica de Negócios:

```text
Desenvolvimento Android (Kotlin/Jetpack Compose). Preciso atualizar o código do meu aplicativo de Relógio Binário com novas funcionalidades específicas.

Aqui estão os requisitos mandatórios:

1. **Lógica do Relógio (Formato BCD):**
   - O relógio deve exibir estritamente: 2 colunas para Horas, 2 colunas para Minutos, 2 colunas para Segundos (Total: 6 colunas verticais).
   - A leitura é feita de baixo para cima (valores: 1, 2, 4, 8).
   - Exemplo: Para 19 horas, a primeira coluna mostra '1' e a segunda mostra '9' (8+1).

2. **Nova Funcionalidade: Modo Noturno e Menu Suspenso:**
   - Crie uma área clicável invisível na parte superior (top 20% da tela).
   - Ao clicar, exiba um menu flutuante (Overlay) com duas opções: "Modo Normal" e "Modo Noturno".
   - **Comportamento do Menu:** Se o usuário não interagir por 5 segundos, o menu deve desaparecer (fade out) automaticamente.
   - **Visual do Modo Noturno:** Deve ser minimalista (inspirado em neon azul sobre fundo preto). Remova todas as legendas, 
   números ou textos auxiliares. Apenas os "pontos" (LEDs) devem ficar visíveis e com alto contraste/brilho.
   - **Visual do Modo Normal:** Mantém as legendas e números auxiliares para facilitar a leitura.

3. **Sistema e Permissões (Keep Screen On):**
   - O aplicativo deve impedir que o celular entre em modo de suspensão/hibernação enquanto estiver aberto.
   - Utilize a flag `WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON` na Activity ou o modificador `keepScreenOn` no Compose. 
   Isso é crucial para usar o app como relógio de mesa.

4. **Persistência de Dados:**
   - O app deve salvar a escolha do usuário (Modo Normal vs. Normal) usando `SharedPreferences` ou `DataStore`. Ao fechar e abrir o app,
   ele deve lembrar do último modo selecionado.

5. **Referência Visual:**
   - Baseie o design dos pontos na imagem anexa (círculos com brilho neon azul, fundo preto profundo).

Gere o código atualizado focando na `MainActivity`, na lógica de desenho do canvas/grid e no gerenciamento de estado para o menu e a persistência.

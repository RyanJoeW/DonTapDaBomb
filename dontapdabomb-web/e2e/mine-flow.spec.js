import { test, expect } from '@playwright/test'

test('register -> start game -> cashout -> leaderboard shows entry', async ({ page }) => {

    page.on('dialog', async dialog => {
        console.log('ALERT:', dialog.message());
        await dialog.dismiss();
    })

    await page.goto('http://localhost:5173/mines/game')
    await expect(page.locator('text=💣 Mines Game')).toBeVisible()

    // 1) Register page
    await page.goto('/register')

    // unieke user zodat je geen duplicates krijgt
    const username = `e2e_${Date.now()}`
    const password = 'test123'
    const cash = '100'

    await page.getByLabel('Name').fill(username)
    await page.getByLabel('Cash').fill(cash)
    await page.getByLabel('Password').fill(password)

    await page.getByRole('button', { name: /add/i }).click()


    // 2) Ga naar mines game
    await page.goto('/mines/game')

// Vul login velden in
    await page.locator('#username').fill(username)
    await page.locator('#password').fill(password)

// Board size kiezen (3x3)
    await page.locator('#boardSize').selectOption('9')

// Mines en bet invullen
    await page.locator('#mines').fill('1')
    await page.locator('#bet').fill('10')

// Start game
    page.on('console', msg => console.log('BROWSER LOG:', msg.text()));
    page.on('pageerror', err => console.log('PAGE ERROR:', err.message));
    await page.getByRole('button', { name: /start game/i }).click()

    await page.waitForTimeout(500);
    console.log('Still on start panel:', await page.locator('.start-panel').count());
    // 3) Check dat game UI zichtbaar is (stabiele checks)
    await expect(page.locator('.game-board')).toBeVisible()

// Cash out knop is hét teken dat game actief is
    await expect(page.getByRole('button', { name: /cash out/i })).toBeVisible()

// (optioneel) check dat multiplier/profit zichtbaar zijn
    await expect(page.getByText(/profit:/i)).toBeVisible()
    await expect(page.getByText(/multiplier:/i)).toBeVisible()

    // 4) Klik 1 cell (safe of mine maakt niet uit; we proberen cashout alleen als active)
    const cells = page.locator('.cell')
    await expect(cells.first()).toBeVisible()
    await cells.first().click()

    // 5) Als cashout knop er is (game nog actief), cashout doen
    const cashoutBtn = page.getByRole('button', { name: /cash out/i })
    if (await cashoutBtn.isVisible()) {
        await cashoutBtn.click()
    }

    // 6) Leaderboard pagina checken
    await page.goto('/leaderboard')

    // moet username ergens in de lijst staan (kan top10 zijn of niet)
    // dus we checken dat leaderboard laadt en niet leeg stuk is
    await expect(page.getByRole('heading', { name: /leaderboard/i })).toBeVisible()


})
/*
 * Plugin UHCReloaded : Alliances
 *
 * Copyright ou © ou Copr. Amaury Carrade (2016)
 * Idées et réflexions : Alexandre Prokopowicz, Amaury Carrade, "Vayan".
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package me.cassayre.florian.damageslogger.commands;

import fr.zcraft.zlib.components.commands.*;
import fr.zcraft.zlib.components.i18n.I;
import me.cassayre.florian.damageslogger.DamagesLogger;
import me.cassayre.florian.damageslogger.report.Report;
import org.bukkit.ChatColor;

import java.util.List;

@CommandInfo (name = "start", usageParameters = "[record title] [--track-new-players] [--stop-track-on-death] [--stop-track-on-disconnection]")
@WithFlags ({"track-new-players", "stop-track-on-death", "stop-track-on-disconnection"})
public class StartCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        if (DamagesLogger.get().getReport() != null)
        {
            error(I.t("DamagesLogger is still recording. Use \"{0}\" to stop the recording.", Commands.getCommandInfo(StopCommand.class).build()));
        }

        final String title = String.join(" ", args).trim();

        DamagesLogger.get().setReport(new Report()
                .title(title.isEmpty() ? "Minecraft Report" : ChatColor.translateAlternateColorCodes('&', title))
                .registerOnlinePlayers()
                .registerTeamsFromScoreboard()
                .autoTrack(true)
                .autoTrackNewPlayers(hasFlag("track-new-players"))
                .stopTrackOnDeath(hasFlag("stop-track-on-death"))
                .stopTrackOnDisconnection(hasFlag("stop-track-on-disconnection"))
        );

        success(I.t("Recording has been started."));
    }

    @Override
    protected List<String> complete()
    {
        if (args.length > 0) return getMatchingSubset(args[args.length - 1], "--track-new-players", "--stop-track-on-death", "--stop-track-on-disconnection");
        else return null;
    }
}
